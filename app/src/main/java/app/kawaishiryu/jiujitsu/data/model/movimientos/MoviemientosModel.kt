package app.kawaishiryu.jiujitsu.data.model.movimientos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Nombre de la tecnica Japones
//Nombre de tecnica Español
//Graduacion

@Parcelize
data class MoviemientosModel(
    var uuId: String = "",
    var nameTec: String = "",
    var transalteTec: String = "",
    var urlYoutube: String = "",
    var description: String = "",
    var grado: String = ""

) : Parcelable {

    companion object {
        const val CLOUD_FIRE_STORE_PATH = "Prueba1"
        const val UUID_KEY = "UUID"
        const val NAME_TEC_KEY = "NAME_TEC"
        const val TRANSLATE_TEC_KEY = "TRANSLATE_TEC"
        const val URL_YOUTUBE_KEY = "URL_YOUTUBE"
        const val DESCRIPTION_KEY = "DESCRIPTION"
        const val GRADO_KEY = "GRADO"
    }

    fun toDictionary(): MutableMap<String, Any> {

        val map = mutableMapOf<String, Any>()

        map[UUID_KEY] = uuId
        map[NAME_TEC_KEY] = nameTec
        map[TRANSLATE_TEC_KEY] = transalteTec
        map[URL_YOUTUBE_KEY] = urlYoutube
        map[DESCRIPTION_KEY] = description
        map[GRADO_KEY] = grado

        return map
    }

}
