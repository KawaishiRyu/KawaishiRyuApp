package app.kawaishiryu.jiujitsu.data.model.service

import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore.CloudFileStoreWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RegisterUserModelService {

    //Registra los datos obtenidos de la otra corutina en firebase
    //Registramos al usuario en la base de datos
    suspend fun register(userModel: UserModel): Void = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.replace(
            UserModel.CLOUD_FIRE_STORE_PATH,
            userModel.id, //uuId as document path of firebase fire store database
            userModel.toDictionary()
        )
    }

    suspend fun registerUser(user: UserModel): String = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.registerComplete(user)
    }

    suspend fun signInUser(user: UserModel): Boolean = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.signInUserComplete(user)
    }

    suspend fun loggedInUser(): Boolean = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.loggedUser()
    }

    suspend fun signOutUser(): Boolean = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.signOut()
    }

    suspend fun dbColletionRefUser(id: String): UserModel = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.obtenerDatosFirebase(UserModel.CLOUD_FIRE_STORE_PATH,id)
    }

    suspend fun modifiedCurrentUser(user: UserModel, id:String): Void = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.modifiedCurrentUser(UserModel.CLOUD_FIRE_STORE_PATH,id,user.toDictionary())
    }

}