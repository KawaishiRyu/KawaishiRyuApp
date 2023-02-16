package app.kawaishiryu.jiujitsu.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUser(
    var id: String = "",
    var name: String = "",
    var apellido: String = "",
    var password:String = "",
    var email:String = "",
    var pictureProfile:String = "",
    var pathPictureProfile: String = ""
):Parcelable

