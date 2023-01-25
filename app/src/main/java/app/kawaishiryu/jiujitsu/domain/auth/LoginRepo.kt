package app.kawaishiryu.jiujitsu.domain.auth

import com.google.firebase.auth.FirebaseUser

interface LoginRepo {

    suspend fun sign(email: String, password: String): FirebaseUser?
}