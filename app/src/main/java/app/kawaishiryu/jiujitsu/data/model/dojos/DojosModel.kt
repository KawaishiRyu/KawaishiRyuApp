package app.kawaishiryu.jiujitsu.data.model.dojos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DojosModel(
    var uuId: String = "",
    var nameSensei: String = "",
    var nameDojo: String = "",
    var dojoUrlImage: String = "",
    var imagePathUrl: String = "",
    var description: String = "",
    var price: String = "",
    //Añadido
    var numberWpp: String = "",
    var instaUrl: String = "",
    var facebookUrl: String = "",
    //Añadido Ubicacion
    var latitud: Double = 0.0,
    var longitud: Double = 0.0

) : Parcelable {

    companion object {
        const val CLOUD_FIRE_STORE_PATH = "DOJOS"
        const val UUID_KEY = "UUID"
        const val NAME_SENSEI_KEY = "NAME_SENSEI"
        const val NAME_DOJO_KEY = "NAME_DOJO"
        const val DOJO_IMAGE_URL_KEY = "DOJO_IMAGE_URL"
        const val DOJO_IMAGE_PATH_URL_KEY = "DOJO_IMAGE_PATH_URL"
        const val DESCRIPTION_KEY = "DESCRIPTION"
        const val PRICE_KEY = "PRICE"

        const val NUMBER_WPP_KEY = "NUMBER_WPP"
        const val INSTA_URL_KEY = "INSTA_URL"
        const val FACEBOOK_URL_KET = "FACEBOOK_URL"

        //Añadimos la latitud y longitud
        const val LATITUD_KEY = "LATITUD_UBICACION"
        const val LONGITUD_KEY = "LONGITUD_UBICACION"
    }


    fun toDictionary(): MutableMap<String, Any> {

        val map = mutableMapOf<String, Any>()

        map[UUID_KEY] = uuId
        map[NAME_SENSEI_KEY] = nameSensei
        map[NAME_DOJO_KEY] = nameDojo
        map[DOJO_IMAGE_URL_KEY] = dojoUrlImage
        map[DOJO_IMAGE_PATH_URL_KEY] = imagePathUrl
        map[DESCRIPTION_KEY] = description
        map[PRICE_KEY] = price

        map[NUMBER_WPP_KEY] = numberWpp
        map[INSTA_URL_KEY] = instaUrl
        map[FACEBOOK_URL_KET] = facebookUrl

        map[LATITUD_KEY] = latitud
        map[LONGITUD_KEY] = longitud

        return map
    }

    fun parcing(map: MutableMap<String, Any>) {
        uuId = map[UUID_KEY] as String
        nameSensei = map[NAME_SENSEI_KEY] as String
        nameDojo = map[NAME_DOJO_KEY] as String
        dojoUrlImage = map[DOJO_IMAGE_URL_KEY] as String
        imagePathUrl = map[DOJO_IMAGE_PATH_URL_KEY] as String
        description = map[DESCRIPTION_KEY] as String
        price = map[PRICE_KEY] as String
    }

}