package app.kawaishiryu.jiujitsu.data.model.service

import android.net.Uri
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DojosModelService {

    //Registra los datos obtenidos de la otra corutina en firebase
    suspend fun register(dojosModel: DojosModel): Void = withContext(Dispatchers.IO){
        return@withContext CloudFileStoreWrapper.replace(
            DojosModel.CLOUD_FIRE_STORE_PATH,
            dojosModel.uuId, //uuId as document path of firebase fire store database
            dojosModel.toDictionary()
        )
    }

    //Sube una imagen a firebase
    suspend fun uploadImageFile(uri: Uri, fileName:String): String = withContext(Dispatchers.IO){
        return@withContext FirebaseStorageManager.uploadImage(
            uri = uri,
            folderName = FirebaseStorageManager.DOJOS_IMAGE_FOLDER,
            fileName = fileName
        )
    }
}