package app.kawaishiryu.jiujitsu.firebase.cloudfirestore

import android.app.DownloadManager.Query
import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.UserModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    suspend fun obtenerDatosFirebase(collectionPath: String, documentPath: String): CurrentUser {
        return suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(collectionPath).document(documentPath).get().addOnSuccessListener { value->
                if (value.exists()){
                    var nombre = value.getString(UserModel.NAME_USER_KEY)
                    var email = value.getString(UserModel.EMAIL_USER_KEY)
                    var picture = value.getString(UserModel.PICTURE_PROFILE_USER_KEY)
                    var password = value.getString(UserModel.PASSWORD_USER_KEY)
                    var valores = CurrentUser(
                        name = nombre!!,
                        email = email!!,
                        pictureProfile = picture!!,
                        password = password!!
                    )
                    continuation.resume(valores)
                }else{
                    //Se vere

            }
            }
        }
    }




            //Corutina que pide datos
    suspend fun replace(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void {

        return suspendCoroutine { continuacion ->
            Firebase.firestore.collection(collectionPath).document(documentPath)
                .set(map)
                .addOnSuccessListener {
                    //Se guarda en la app
                    continuacion.resume(it)
                }
                .addOnFailureListener {
                    continuacion.resumeWithException(it)
                }
        }
    }


    //Esta funcion lo que hace es registrar al usuario y avisar si esta lista o no
    suspend fun registerComplete(user: CurrentUser): String {
        return suspendCoroutine { continuacion ->
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        var user = firebaseAuth.currentUser
                        var uuid = user!!.uid
                        Log.i("usuario registrado", "${user!!.email}")
                        Log.i("usuario registrado2", "${user.uid}")
                        continuacion.resume(uuid)
                        //continuacion.resume(UserRegisterId(registerSuccesfully = task.isComplete, id = uuid))
                    }

                }.addOnFailureListener {
                    Log.i("registrarUsuarioError", "${it}")
                    continuacion.resumeWithException(it)
                }

        }
    }

    //Esta funcion nos sirve para poder ingresar de usuario

    suspend fun signInUserComplete(user: CurrentUser): Boolean {
        return suspendCoroutine { continuacion ->

            firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuacion.resume(task.isComplete)
                    }
                }.addOnFailureListener { error ->
                    //Error de cuando logra ingresar
                    continuacion.resumeWithException(error)
                }


        }
    }


    //Esta funcion suspendida nos sierve para retornar verdaddero en el caso
    //de que el usuario ya se encuentre activo

    suspend fun loggedUser(): Boolean {
        return suspendCoroutine { continuacion ->
            if (firebaseAuth.currentUser != null) {
                Log.i("Nombre", "${firebaseAuth.currentUser!!.email}")
                continuacion.resume(true)
            } else {
                //error no se encuentra
                continuacion.resume(false)
            }
        }
    }

    suspend fun signOut(): Boolean {
        return suspendCoroutine { continuation ->
            firebaseAuth.signOut()
            continuation.resume(true)
        }
    }

    /*
    val db = Firebase.firestore
val docRef = db.collection("tu_coleccion").document("tu_documento")
     */
    //Funcion que nos dirà si el cambio fue realizado e existoso
    suspend fun modifiedCurrentUser(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void {
        //La funcion setoptions.merge solo actualiza valores que se cambiaron
        return suspendCoroutine { continuation ->
            Log.i("mapeo","$map")
            Firebase.firestore.collection(collectionPath).document(getUUIDUser()).set(
                map,
                SetOptions.merge()
            ).addOnSuccessListener {
                continuation.resume(it)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }
}


    fun getUUIDUser() = firebaseAuth.currentUser!!.uid


}








