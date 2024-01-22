package app.kawaishiryu.jiujitsu.data.model.service

import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore.CloudFileStoreWrapper
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object DojosModelService {

    suspend fun registerWithJson(dojosModel: DojosModel, data: HashMap<String, String>): Void =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.replaceWithJson(
                DojosModel.CLOUD_FIRE_STORE_PATH,
                dojosModel.uuId, //uuId as document path of firebase fire store database
                data
            )
        }

    suspend fun updateDojoFromFirebase(dojosModel: DojosModel, data: HashMap<String, String>) =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.updateWithJson(
                DojosModel.CLOUD_FIRE_STORE_PATH,
                dojosModel.uuId,
                data
            )
        }

    suspend fun deleteDojoFromFirebase(dojosModel: DojosModel) = withContext(Dispatchers.IO) {
        return@withContext CloudFileStoreWrapper.deleteDocumentoFirebase(
            DojosModel.CLOUD_FIRE_STORE_PATH,
            dojosModel.uuId
        )
    }

    suspend fun getListFromFirebase(): MutableList<DojosModel> = withContext(Dispatchers.IO) {
        val db = Firebase.firestore
        val data = mutableListOf<DojosModel>()

        try {
            val result = db.collection(DojosModel.CLOUD_FIRE_STORE_PATH).get().await()

            for (document in result) {

                val jsonField = document.getString("jsonData") ?: "{}" // Puedes proporcionar un JSON vacío como valor predeterminado
                val gson = Gson()
                val jsonData: DojosModel = gson.fromJson(jsonField, DojosModel::class.java)

                //FALTA CORREGIR HORARIOS
                data.add(
                    DojosModel(
                        uuId = jsonData.uuId,
                        nameSensei = jsonData.nameSensei,
                        dojoUrlImage = jsonData.dojoUrlImage,
                        nameDojo = jsonData.nameDojo,
                        description = jsonData.description,
                        price = jsonData.price,
                        numberWpp = jsonData.numberWpp,
                        facebookUrl = jsonData.facebookUrl,
                        instaUrl = jsonData.instaUrl,
                        latitud = jsonData.latitud,
                        longitud = jsonData.longitud,
                        imagePathUrl = jsonData.imagePathUrl,
                        horarios = jsonData.horarios
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