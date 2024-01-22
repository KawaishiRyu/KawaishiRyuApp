package app.kawaishiryu.jiujitsu.data.model.service

import android.graphics.Bitmap
import app.kawaishiryu.jiujitsu.repository.firebase.storage.FirebaseStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageService {

    //Sube una imagen a firebase
    suspend fun uploadImageFile(uri: Bitmap, fileName: String, folderName: String): String =
        withContext(Dispatchers.IO) {
            return@withContext FirebaseStorageManager.uploadImage(
                bitmap = uri,
                folderName,
                fileName = fileName
            )
        }
}