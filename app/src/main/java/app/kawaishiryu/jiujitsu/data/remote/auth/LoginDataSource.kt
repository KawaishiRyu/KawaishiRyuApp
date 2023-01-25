package app.kawaishiryu.jiujitsu.data.remote.auth

import android.provider.ContactsContract.CommonDataKinds.Email
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


//Logica para loguear el usuario - Peticion a firebase
class LoginDataSource (private val dataSource: LoginDataSource): LoginRepo{

    //Hacemos consulta y con el await esperamos a q traiga la informacion del servidor
    override suspend fun sign(email: String, password: String): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
        return authResult.user
    }


}