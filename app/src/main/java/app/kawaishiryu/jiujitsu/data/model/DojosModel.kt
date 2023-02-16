package app.kawaishiryu.jiujitsu.data.model

class DojosModel {

    companion object {
        const val CLOUD_FIRE_STORE_PATH = "DOJOS"
        const val UUID_KEY = "UUID"
        const val NAME_SENSEI_KEY = "NAME_SENSEI"
        const val NAME_DOJO_KEY = "NAME_DOJO"
        const val DOJO_IMAGE_URL_KEY = "DOJO_IMAGE_URL"
        const val DESCRIPTION_KEY = "DESCRIPTION"
        const val PRICE_KEY = "PRICE"
    }

    var uuId = ""
    var nameSensei = ""
    var nameDojo = ""
    var dojoUrlImage = ""
    var description = ""
    var price = ""

    fun toDictionary(): MutableMap<String, Any> {

        val map = mutableMapOf<String, Any>()

        map[UUID_KEY] = uuId
        map[NAME_SENSEI_KEY] = nameSensei
        map[NAME_DOJO_KEY] = nameDojo
        map[DOJO_IMAGE_URL_KEY] = dojoUrlImage
        map[DESCRIPTION_KEY] = description
        map[PRICE_KEY] = price

        return map
    }

    fun parcing(map: MutableMap<String, Any>) {
        uuId = map[UUID_KEY] as String
        nameSensei = map[NAME_SENSEI_KEY] as String
        nameDojo = map[NAME_DOJO_KEY] as String
        dojoUrlImage = map[DOJO_IMAGE_URL_KEY] as String
        description = map[DESCRIPTION_KEY] as String
        price = map[PRICE_KEY] as String
    }

}