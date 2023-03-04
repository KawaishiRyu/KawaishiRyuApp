package app.kawaishiryu.jiujitsu.data.model.service

import android.app.DownloadManager.Query
import android.net.Uri
import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.Current

object RegisterModelService {

    //Registra los datos obtenidos de la otra corutina en firebase
    //Registramos al usuario en la base de datos
    suspend fun register(userModel: UserModel): Void = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.replace(
            UserModel.CLOUD_FIRE_STORE_PATH,
            userModel.currentUser.id, //uuId as document path of firebase fire store database
            userModel.toDictionary()
        )
    }

    suspend fun registerUser(user: CurrentUser): String = withContext(Dispatchers.IO){
        Log.i("registrarUser","llego hasta aqui 2")
        return@withContext CloudFileStoreWrapper.registerComplete(user)
    }

    suspend fun signInUser(user: CurrentUser): Boolean = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.signInUserComplete(user)
    }

    suspend fun loggedInUser(): Boolean = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.loggedUser()
    }

    suspend fun signOutUser(): Boolean = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.signOut()
    }

    suspend fun dbColletionRefUser(id: String): CurrentUser = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.obtenerDatosFirebase(UserModel.CLOUD_FIRE_STORE_PATH,id)
    }

    //utuilizamos una funcion suspendida
    suspend fun modifiedCurrentUser(user: UserModel, id:String): Void = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.modifiedCurrentUser(UserModel.CLOUD_FIRE_STORE_PATH,id,user.toDictionary())
    }

}