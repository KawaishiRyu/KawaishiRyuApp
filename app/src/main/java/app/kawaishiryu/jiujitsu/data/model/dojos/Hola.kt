package app.kawaishiryu.jiujitsu.data.model.dojos

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun main(args: Array<String>) {
    val gson = Gson()
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()

    val tut = DojosModel("1", "efra", "asfsasd","asdasd","asdasd")

    val jsonTut: String = gson.toJson(tut)
    println(jsonTut)

    val jsonTutPretty: String = gsonPretty.toJson(tut)
    println(jsonTutPretty)

}




