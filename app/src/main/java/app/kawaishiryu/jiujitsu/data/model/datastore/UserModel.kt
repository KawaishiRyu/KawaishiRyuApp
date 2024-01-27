package app.kawaishiryu.jiujitsu.data.model.datastore

data class UserModelDataStore(val name: String, val rol: String) {
    companion object {
        const val KEY_USER = "user_key"
        const val KEY_CHECKED = "checked_key"
    }
}