package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import com.google.gson.Gson

object ModelToJson {

    // Define una función de extensión genérica para cualquier modelo
    inline fun <reified T> T.toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    // Define una función de extensión genérica para cualquier modelo que convierte el JSON en un HashMap
    inline fun <reified T> T.toHashMap(): HashMap<String, String> {
        val jsonString = this.toJson()
        return hashMapOf("jsonData" to jsonString)
    }
}