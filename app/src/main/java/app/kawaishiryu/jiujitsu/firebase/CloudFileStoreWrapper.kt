package app.kawaishiryu.jiujitsu.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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