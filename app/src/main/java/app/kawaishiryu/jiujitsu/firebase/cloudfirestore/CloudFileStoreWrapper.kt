package app.kawaishiryu.jiujitsu.firebase.cloudfirestore

import android.app.DownloadManager.Query
import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.DojosModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CloudFileStoreWrapper {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }


    //Corutina que pide datos
    suspend fun replace(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void{

        return suspendCoroutine { continuacion->
            Firebase.firestore.collection(collectionPath).document(documentPath).set(map)
                .addOnSuccessListener {
                    continuacion.resume(it)
                }
                .addOnFailureListener {
                    continuacion.resumeWithException(it)
                }
        }
    }

    suspend fun select(collectionPath: String, conditionMap: MutableMap<String, Any>?, limit: Long = 1){
        return suspendCoroutine {
            val collectionReference = Firebase.firestore.collection(collectionPath)
            var query = collectionReference.limit(limit)

            conditionMap?.let {
                it.forEach {map->
                    query = collectionReference.whereEqualTo(map.key,map.value)
                }
            }

            query.get().addOnSuccessListener {

            }.addOnFailureListener {

            }
        }

    }

    //Esta funcion lo que hace es registrar al usuario y avisar si esta lista o no
    suspend fun registerComplete(user: CurrentUser): Void{
        return suspendCoroutine { continuacion ->
            firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener { task->
                if(task.isSuccessful){

                    var user = firebaseAuth.currentUser
                    Log.i("usuario registrado","${user!!.email}")
                    Log.i("usuario registrado2", "${user.uid}")
                }

            }.addOnFailureListener {
                Log.i("registrarUsuarioError","${it}")
            }

        }
    }

}

