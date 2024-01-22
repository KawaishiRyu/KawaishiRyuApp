package app.kawaishiryu.jiujitsu.data.model.service

import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore.CloudFileStoreWrapper
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object RegisterTecService {

    suspend fun recordWithJson(
        PATH: String,
        model: MoviemientosModel,
        data: HashMap<String, String>
    ): Void =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.replaceWithJson(
                PATH,
                model.uuId, //uuId as document path of firebase fire store database
                data
            )
        }

    suspend fun getListFromFribaseTec(path: String): MutableList<MoviemientosModel> =
        withContext(Dispatchers.IO) {
            val db = Firebase.firestore
            val data = mutableListOf<MoviemientosModel>()

            try {
                val result = db.collection(path).get().await()

                for (document in result) {

                    val jsonField = document.getString("jsonData")
                        ?: "{}" // Puedes proporcionar un JSON vacío como valor predeterminado

                    val gson = Gson()
                    val jsonData: MoviemientosModel = gson.fromJson(jsonField, MoviemientosModel::class.java)

                    data.add(
                        MoviemientosModel(
                            uuId = jsonData.uuId,
                            nameTec = jsonData.nameTec,
                            transalteTec = jsonData.transalteTec,
                            urlYoutube = jsonData.urlYoutube,
                            description = jsonData.description,
                            grado = jsonData.grado
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d("???", "Error")
            }
            return@withContext data
        }

    suspend fun deleteTecFromFirebase(nameDocId: String, tecModel: MoviemientosModel) =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.deleteDocumentoFirebase(
                nameDocId,
                tecModel.uuId
            )
        }

}