package app.kawaishiryu.jiujitsu.data.model.service

import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore.CloudFileStoreWrapper
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//CAPA DE SERVICIO DE USUARIO
//La práctica de separar la lógica de esa manera se conoce comúnmente como el principio de separación
//de preocupaciones o "Separation of Concerns" en inglés. Este principio sugiere dividir un programa en
//partes distintas, cada una de las cuales aborda una preocupación específica.
/*
* Ventajas de la Separación de Concerns:

Mantenibilidad: Al separar las preocupaciones, cada componente puede ser más fácilmente entendido y mantenido. Cambios en una parte del sistema no deberían afectar innecesariamente a otras partes.

Reusabilidad: Las capas de abstracción pueden ser reutilizadas en diferentes partes del programa o incluso en diferentes proyectos, ya que encapsulan la funcionalidad de manera independiente.

Testabilidad: La separación facilita la creación de pruebas unitarias y de integración, ya que cada componente puede ser probado de forma aislada.

Escalabilidad: Facilita la escalabilidad del sistema ya que cada componente puede ser gestionado y escalado de manera independiente.

Colaboración: Permite que equipos diferentes trabajen en diferentes partes del sistema sin interferencias innecesarias.
*/

object UserModelService {

    //Crear coleccion en Firebase
    suspend fun registerWithJson(user: UserModel, data: HashMap<String, String>): Void =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.replaceWithJson(
                UserModel.CLOUD_FIRE_STORE_PATH,
                user.id, //uuId as document path of firebase fire store database
                data
            )
        }

    //RegisterByEmailAndPassword
    suspend fun registerUser(user: UserModel): String = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.registerComplete(user)
    }

    //LogIn with email
    suspend fun signInUser(user: UserModel): Boolean = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.signInUserComplete(user)
    }

    //Log with google
    suspend fun signInWithGoogle(account: GoogleSignInAccount?): Pair<UserModel, Boolean> = withContext(Dispatchers.IO) {
        // Obtener el token de ID de Google desde la cuenta
        val idToken = account?.idToken
        return@withContext CloudFileStoreWrapper.signInWithGoogleComplete(idToken)
    }

    //Verifica si hay un usuario conectado
    suspend fun loggedInUser(): Boolean = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.loggedUser()
    }

    //Sign off
    suspend fun signOutUser(): Boolean = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.signOut()
    }


    suspend fun modifiedCurrentUser(id: String, data: HashMap<String, String>): Void =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.modifiedCurrentUser(
                UserModel.CLOUD_FIRE_STORE_PATH,
                id,
                data
            )
        }

    suspend fun getUserFromFirebaseById(id: String): UserModel =
        CloudFileStoreWrapper.getUserFromFirebaseById(id)


    // Función para verificar la existencia de un usuario en la base de datos
    suspend fun checkIfUserExists(userId: String): Boolean {
        return CloudFileStoreWrapper.isUserExist(userId)
    }
}