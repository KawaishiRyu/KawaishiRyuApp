package app.kawaishiryu.jiujitsu.presentation.auth

import android.util.Log
import androidx.lifecycle.*
import app.kawaishiryu.jiujitsu.data.LoginResult
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepo
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepoImpl
import com.google.firebase.auth.FirebaseUser
import io.grpc.internal.SharedResourceHolder.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginScreenViewModel(private val repo: LoginRepo) : ViewModel() {


    fun signIn(email: String, password: String): LiveData<LoginResult<FirebaseUser?>>  =
        liveData(viewModelScope.coroutineContext + Dispatchers.Main) {

            emit(LoginResult.Loading())

            try {
                emit(LoginResult.Success(repo.sign(email, password)))

            } catch (e: Exception) {

                emit(LoginResult.Failure(e))
            }

        }

}

/*class LoginScreenViewModelFactory(private val repo: LoginRepoImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  modelClass.getConstructor(LoginRepoImpl::class.java).newInstance(repo)
    }

}*/

class LoginScreenViewModelFactory(private val repo: LoginRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LoginRepo::class.java).newInstance(repo)
    }
}