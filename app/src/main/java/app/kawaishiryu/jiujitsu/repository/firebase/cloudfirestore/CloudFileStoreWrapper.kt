package app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore

import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//CAPA DE ACCESO A DATOS FIREBASE
//Capa de abstraccion q actua como intermediario entre la logica de negocio y la implementacion
//Capa de Acceso a Datos (CloudFileStoreWrapper):
//
//Definición: Se encarga de interactuar con las fuentes de datos, como Firebase Firestore, y realiza
//operaciones de lectura y escritura.
//Responsabilidades: Implementa las operaciones específicas de acceso a datos y devuelve los resultados
//a la capa de servicio.

object CloudFileStoreWrapper {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    suspend fun replaceWithJson(
        collectionPath: String,
        documentPath: String,
        map: HashMap<String, String>
    ): Void {
        return suspendCoroutine { continuacion ->
            Firebase.firestore.collection(collectionPath).document(documentPath)
                .set(map)
                .addOnSuccessListener {
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

    //Crea un usuario a partir de usuario y contraseña
    suspend fun registerComplete(user: UserModel): String {
        return suspendCoroutine { continuacion ->
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val uuid = user!!.uid
                        continuacion.resume(uuid)
                        Log.d("???", "Registro completo")
                    }
                }
                .addOnFailureListener {
                    Log.d("???", "Error con error: $it")
                    continuacion.resumeWithException(it)
                }
        }
    }

    //Funcion q inicia sesion con email@
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

    suspend fun signInWithGoogleComplete(idToken: String?): Pair<UserModel, Boolean>  {
        return suspendCoroutine { continuation ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val uuid = user?.uid ?: ""
                        val displayName = user?.displayName ?: ""
                        val email = user?.email ?: ""

                        val names = displayName.split(" ")
                        val firstName = names.firstOrNull() ?: ""
                        val lastName = names.drop(1).joinToString(" ")

                        val userModel = UserModel(
                            id = uuid,
                            name = firstName,
                            apellido = lastName,
                            email= email)

                        continuation.resume(Pair(userModel, true))

                    } else {
                        continuation.resume(Pair(UserModel(), false))
                    }
                }
        }
    }

    //Verifica si hay un usuario autenticado en firebase
    suspend fun loggedUser(): Boolean {
        return suspendCoroutine { continuacion ->
            if (firebaseAuth.currentUser != null) {
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

    //Verificamos si exite Usuario existe
    suspend fun isUserExist(userId: String): Boolean {
        return suspendCoroutine { continuation ->
            Firebase.firestore.collection(UserModel.CLOUD_FIRE_STORE_PATH).document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    continuation.resume(documentSnapshot.exists())
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    suspend fun getUserFromFirebaseById(id: String): UserModel =
        suspendCoroutine { continuation ->
            Firebase.firestore.collection(UserModel.CLOUD_FIRE_STORE_PATH).document(id)
                .get()
                .addOnSuccessListener { result ->
                    if (result.exists()) {
                        val jsonField = result.getString("jsonData") ?: "{}"
                        val gson = Gson()
                        val jsonData: UserModel = gson.fromJson(jsonField, UserModel::class.java)

                        val data = UserModel(
                            name = jsonData.name,
                            email = jsonData.email,
                            apellido = jsonData.apellido,
                            id = jsonData.id,
                            rol = jsonData.rol,
                            pathPictureProfile = jsonData.pathPictureProfile,
                            pictureProfile = jsonData.pictureProfile
                        )
                        continuation.resume(data)
                    } else {
                        continuation.resume(UserModel()) // Valor predeterminado si el documento no existe
                    }
                }
                .addOnFailureListener {
                    Log.e("???", "Error: ${it.message}", it)
                    continuation.resume(UserModel()) // Manejar la excepción y devolver un valor predeterminado
                }
        }

    fun getCurrentUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }


    //Corutina que actualiza datos
    suspend fun modifiedCurrentUser(
        collectionPath: String,
        documentPath: String,
        map: HashMap<String, String>
    ): Void {
        return suspendCoroutine { continuation ->
            Log.i("???", "$map")
            Firebase.firestore.collection(collectionPath)
                .document(getCurrentUserId()!!)
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
}








