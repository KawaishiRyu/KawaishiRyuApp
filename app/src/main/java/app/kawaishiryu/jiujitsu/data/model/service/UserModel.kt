package app.kawaishiryu.jiujitsu.data.model.service

import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import com.google.firebase.firestore.auth.User

class UserModel {

    companion object {
        const val CLOUD_FIRE_STORE_PATH = "USER"
        const val ID_KEY = "ID"
        const val NAME_USER_KEY = "NAME_USER"
        const val PASSWORD_USER_KEY = "PASSWORD_USER"
        const val EMAIL_USER_KEY = "EMAIL_USER"
        const val PICTURE_PROFILE_USER_KEY = "PICTURE_PROFILE_USER"
        const val PATH_USER_key = "PATH"


    }

    var id = ""
    var nameUser = ""
    var passwordUser = ""
    var emailUser = ""
    var pictureProfileUser = ""
    var pathUser = ""



    fun toDictionary(): MutableMap<String, Any> {

        val map = mutableMapOf<String, Any>()

        map[ID_KEY] = id
        map[NAME_USER_KEY] = nameUser
        map[PASSWORD_USER_KEY] = passwordUser
        map[EMAIL_USER_KEY] = emailUser
        map[PICTURE_PROFILE_USER_KEY] = pictureProfileUser
        map[PATH_USER_key] = pathUser

        return map
    }

    fun parcing(map: MutableMap<String, Any>): CurrentUser {
        id = map[ID_KEY] as String
        nameUser = map[NAME_USER_KEY] as String
        passwordUser = map[PASSWORD_USER_KEY] as String
        emailUser = map[EMAIL_USER_KEY] as String
        pictureProfileUser = map[PICTURE_PROFILE_USER_KEY] as String
        pathUser = map[PATH_USER_key] as String

        return CurrentUser(
            id = id,
            name = nameUser,
            apellido = "",
            password = passwordUser,
            email = emailUser,
            pictureProfile = pictureProfileUser,
            pathPictureProfile = pathUser
        )
    }
}