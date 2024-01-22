package app.kawaishiryu.jiujitsu.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserModel(
    var id: String = "",
    var name: String = "",
    var apellido: String = "",
    var password:String = "",
    var email:String = "",
    var pictureProfile:String = "",
    var pathPictureProfile: String = "",
    var rol: String = ""

) : Parcelable {

    companion object {
        val  userRegister = UserModel()
        const val CLOUD_FIRE_STORE_PATH = "USER"
        const val ID_KEY = "ID"
        const val NAME_USER_KEY = "NAME_USER"
        const val PASSWORD_USER_KEY = "PASSWORD_USER"
        const val EMAIL_USER_KEY = "EMAIL_USER"
        const val PICTURE_PROFILE_USER_KEY = "PICTURE_PROFILE_USER"
    }

}