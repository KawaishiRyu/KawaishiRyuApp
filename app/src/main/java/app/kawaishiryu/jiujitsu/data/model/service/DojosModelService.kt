package app.kawaishiryu.jiujitsu.data.model.service

import android.net.Uri
import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    //Sube una imagen a firebase
    suspend fun uploadImageFile(uri: Uri, fileName: String): String = withContext(Dispatchers.IO) {
        return@withContext FirebaseStorageManager.uploadImage(
            uri = uri,
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
                val nameSensei = document.getString(DojosModel.NAME_SENSEI_KEY) ?: ""
                val dojoUrlImage = document.getString(DojosModel.DOJO_IMAGE_URL_KEY) ?: ""
                val nameDojo = document.getString(DojosModel.NAME_DOJO_KEY) ?: ""
                val description= document.getString(DojosModel.DESCRIPTION_KEY) ?: ""
                val price= document.getString(DojosModel.PRICE_KEY)  ?: ""

                val numberWpp= document.getString(DojosModel.NUMBER_WPP_KEY)  ?: ""
                val facebookUrl= document.getString(DojosModel.FACEBOOK_URL_KET)  ?: ""
                val instaUrl= document.getString(DojosModel.INSTA_URL_KEY)  ?: ""

                data.add(
                    DojosModel(
                        uuId = uuId,
                        nameSensei = nameSensei,
                        dojoUrlImage = dojoUrlImage,
                        nameDojo = nameDojo,
                        description = description,
                        price = price,
                        numberWpp = numberWpp,
                        facebookUrl = facebookUrl,
                        instaUrl = instaUrl
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