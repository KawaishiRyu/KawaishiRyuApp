package app.kawaishiryu.jiujitsu.domain.auth

import app.kawaishiryu.jiujitsu.data.remote.auth.LoginDataSource
import com.google.firebase.auth.FirebaseUser

//Repositorio q implementa la interfaz
//dataSource es de donde vamos a hacer la llamada al servidor
class LoginRepoImpl(private val dataSource: LoginDataSource): LoginRepo {

    override suspend fun sign(email: String, password: String): FirebaseUser? {
        return dataSource.sign(email, password)
    }
}