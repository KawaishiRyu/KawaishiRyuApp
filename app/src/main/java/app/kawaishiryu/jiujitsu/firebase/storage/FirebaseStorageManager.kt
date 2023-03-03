package app.kawaishiryu.jiujitsu.firebase.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FirebaseStorageManager {

    private const val FIREBASE_STORAGE_DOMAIN = "gs://jiu-jitsu-a3585.appspot.com"

    const val DOJOS_IMAGE_FOLDER = "DojosImages/"

    //Cambiamos el imageUri por Bitmap
    suspend fun uploadImage(bitmap: Bitmap, folderName: String, fileName: String): String {

        return suspendCoroutine { continuation ->

            val storageRef = Firebase.storage(FIREBASE_STORAGE_DOMAIN).reference
            val storageReference: StorageReference = storageRef.child("$folderName/$fileName")

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            //Cambie el putFile por putBytes
            val uploadTask: UploadTask = storageReference.putBytes(data)

            uploadTask
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
                .addOnSuccessListener {
                    it.task.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { exception ->
                                continuation.resumeWithException(exception)
                            }
                        }
                        return@Continuation storageReference.downloadUrl
                    }).addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val downloadUri = uriTask.result
                            continuation.resume(downloadUri.toString())
                        }
                    }
                }

        }
    }



}