package app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore

import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.user.UserModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CloudFileStoreWrapper {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    //De Usuario
    suspend fun obtenerDatosFirebase(collectionPath: String, documentPath: String): UserModel {
        return suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(collectionPath).document(documentPath).get()
                .addOnSuccessListener { value ->
                    if (value.exists()) {
                        val nombre = value.getString(UserModel.NAME_USER_KEY)
                        val email = value.getString(UserModel.EMAIL_USER_KEY)
                        val picture = value.getString(UserModel.PICTURE_PROFILE_USER_KEY)
                        val valores = UserModel(
                            name = nombre!!,
                            email = email!!,
                            pictureProfile = picture!!,
                        )
                        continuation.resume(valores)
                    } else {
                        //Se vere
                    }
                }
                .addOnFailureListener{
                    Log.d("???", "Error: $it")
                }
        }
    }

    //Corutina que pide datos en los campos
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

    suspend fun replaceWithJson(
        collectionPath: String,
        documentPath: String,
        map: HashMap<String, String>
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

    suspend fun updateWithJson(
        collectionPath: String,
        documentPath: String,
        map: HashMap<String, String>
    ): Void {
        return suspendCoroutine { continuacion ->
            Firebase.firestore.collection(collectionPath).document(documentPath)
                .set(map, SetOptions.merge())
                .addOnSuccessListener {
                    continuacion.resume(it)
                }
                .addOnFailureListener {
                    continuacion.resumeWithException(it)
                }
        }
    }

    //Esta funcion lo que hace es registrar al usuario y avisar si esta lista o no
    suspend fun registerComplete(user: UserModel): String {
        return suspendCoroutine { continuacion ->
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val uuid = user!!.uid
                        continuacion.resume(uuid)
                    }

                }.addOnFailureListener {
                    Log.d("registrarUsuarioError", "${it}")
                    continuacion.resumeWithException(it)
                }
        }
    }

    //Esta funcion nos sirve para poder ingresar de usuario
    suspend fun signInUserComplete(user: UserModel): Boolean {
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

    //Esta funcion suspendida nos sierve para retornar verdadero en el caso
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

    //Corutina que actualiza datos
    suspend fun modifiedCurrentUser(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void {
        //La funcion setoptions.merge solo actualiza valores que se cambiaron
        return suspendCoroutine { continuation ->
            Log.i("mapeo", "$map")
            Firebase.firestore.collection(collectionPath)
                .document(getUUIDUser())
                .set(
                    map,
                    SetOptions.merge()
                ).addOnSuccessListener {
                    continuation.resume(it)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    //Obtener el uid del usuario
    fun getUUIDUser() = firebaseAuth.currentUser!!.uid

    //Eliminar un documento en firebase
    suspend fun deleteDocumentoFirebase(collectionPath: String, documentPath: String) {
        return suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(collectionPath).document(documentPath)
                .delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}








