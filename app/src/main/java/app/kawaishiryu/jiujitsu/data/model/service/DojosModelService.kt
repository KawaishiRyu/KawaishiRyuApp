package app.kawaishiryu.jiujitsu.data.model.service

import android.graphics.Bitmap
import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object DojosModelService {

    //Registra los datos obtenidos de la otra corutina en firebase
    suspend fun register(dojosModel: DojosModel): Void = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.replace(
            DojosModel.CLOUD_FIRE_STORE_PATH,
            dojosModel.uuId, //uuId as document path of firebase fire store database
            dojosModel.toDictionary()
        )
    }

    suspend fun recordWithJson(dojosModel: DojosModel, data: HashMap<String, String>): Void = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.replaceWithJson(
            DojosModel.CLOUD_FIRE_STORE_PATH,
            dojosModel.uuId, //uuId as document path of firebase fire store database
            data
        )
    }

    suspend fun deleteDojoFromFirebase(dojosModel: DojosModel) = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.deleteDocumentoFirebase(
            DojosModel.CLOUD_FIRE_STORE_PATH,
            dojosModel.uuId
        )
    }

    //Sube una imagen a firebase
    suspend fun uploadImageFile(uri: Bitmap, fileName: String): String =
        withContext(Dispatchers.IO) {
            return@withContext FirebaseStorageManager.uploadImage(
                bitmap = uri,
                folderName = FirebaseStorageManager.DOJOS_IMAGE_FOLDER,
                fileName = fileName
            )
        }


    suspend fun getListFromFirebase(): MutableList<DojosModel> = withContext(Dispatchers.IO) {
        val db = Firebase.firestore
        val data = mutableListOf<DojosModel>()

        try {
            val result = db.collection(DojosModel.CLOUD_FIRE_STORE_PATH).get().await()

            for (document in result) {

                val uuId = document.getString(DojosModel.UUID_KEY) ?: ""
                val jsonField = document.getString("jsonData") ?: "{}" // Puedes proporcionar un JSON vacío como valor predeterminado

                val gson = Gson()
                val jsonData: DojosModel = gson.fromJson(jsonField, DojosModel::class.java)

                jsonData.nameSensei

                data.add(
                    DojosModel(
                        uuId = jsonData.uuId,
                        nameSensei =  jsonData.nameSensei,
                        dojoUrlImage = jsonData.dojoUrlImage,
                        nameDojo = jsonData.nameDojo,
                        description = jsonData.description,
                        price = jsonData.price,
                        numberWpp = jsonData.numberWpp,
                        facebookUrl = jsonData.facebookUrl,
                        instaUrl = jsonData.instaUrl,
                        latitud = jsonData.latitud,
                        longitud = jsonData.longitud
                    )
                )
            }
        } catch (e: Exception) {
            // manejar la excepción
            Log.d("???", "Error")
        }
        return@withContext data
    }

}