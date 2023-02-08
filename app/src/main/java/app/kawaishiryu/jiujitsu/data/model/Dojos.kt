package app.kawaishiryu.jiujitsu.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Dojos(
    val id: String? = null,
    val nameSensei: String? = null,
    val nameDojo: String? = null,
    var urlImageDojo: String? = null,
    val description: String? = null,
    val ubication: String? = null
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "id" to id,
            "nameSensei" to nameSensei,
            "nameDojo" to nameDojo,
            "urlImageDojo" to urlImageDojo,
            "description" to description,
            "ubication" to ubication
        )
    }
}