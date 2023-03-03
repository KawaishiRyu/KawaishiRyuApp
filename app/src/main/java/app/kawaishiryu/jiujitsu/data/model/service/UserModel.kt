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

    }

    var currentUser: CurrentUser = CurrentUser()


    fun toDictionary(): MutableMap<String, Any> {

        val map = mutableMapOf<String, Any>()

        map[ID_KEY] = currentUser.id
        map[NAME_USER_KEY] = currentUser.name
        map[PASSWORD_USER_KEY] = currentUser.password
        map[EMAIL_USER_KEY] = currentUser.email
        //Esta en formato bit
        map[PICTURE_PROFILE_USER_KEY] = currentUser.pictureProfile
        return map
    }

}