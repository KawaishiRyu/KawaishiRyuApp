package app.kawaishiryu.jiujitsu.data.model.service

import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object RegisterTecService {

    suspend fun register(model: MoviemientosModel, PATH: String): Void =
        withContext(Dispatchers.IO) {
            return@withContext CloudFileStoreWrapper.replace(
                PATH,
                model.uuId,
                model.toDictionary()
            )
        }

    suspend fun getListFromFribaseTec(path: String): MutableList<MoviemientosModel> =
        withContext(Dispatchers.IO) {
            val db = Firebase.firestore
            val data = mutableListOf<MoviemientosModel>()

            try {
                val result = db.collection(path).get().await()

                for (document in result) {
                    val uuid = document.getString(MoviemientosModel.UUID_KEY) ?: ""
                    val nameTec = document.getString(MoviemientosModel.NAME_TEC_KEY) ?: ""
                    val translate = document.getString(MoviemientosModel.TRANSLATE_TEC_KEY) ?: ""
                    val url_youtube = document.getString(MoviemientosModel.URL_YOUTUBE_KEY) ?: ""
                    val description = document.getString(MoviemientosModel.DESCRIPTION_KEY) ?: ""
                    val grado = document.getString(MoviemientosModel.GRADO_KEY) ?: ""

                    data.add(
                        MoviemientosModel(
                            uuId = uuid,
                            nameTec = nameTec,
                            transalteTec = translate,
                            urlYoutube = url_youtube,
                            description = description,
                            grado = grado
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