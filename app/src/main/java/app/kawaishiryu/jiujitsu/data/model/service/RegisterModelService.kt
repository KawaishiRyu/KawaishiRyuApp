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
    suspend fun register(userModel: UserModel): Void = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.replace(
            UserModel.CLOUD_FIRE_STORE_PATH,
            userModel.id, //uuId as document path of firebase fire store database
            userModel.toDictionary()
        )
    }

    suspend fun registerUser(user: CurrentUser): Void = withContext(Dispatchers.IO){
        Log.i("registrarUser","llego hasta aqui 2")
        return@withContext CloudFileStoreWrapper.registerComplete(user)
    }

}