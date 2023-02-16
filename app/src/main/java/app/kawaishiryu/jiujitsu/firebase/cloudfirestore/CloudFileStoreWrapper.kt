package app.kawaishiryu.jiujitsu.firebase.cloudfirestore

import android.app.DownloadManager.Query
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.suspendCoroutine

object CloudFileStoreWrapper {

    //Corutina que pide datos
    suspend fun replace(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void{

        return suspendCoroutine {
            Firebase.firestore.collection(collectionPath).document(documentPath).set(map)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }

}