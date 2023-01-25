package app.kawaishiryu.jiujitsu.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepo
import kotlinx.coroutines.Dispatchers


class LoginScreenViewModel (private val repo: LoginRepo): ViewModel() {
    fun signIn(email: String, password: String) = liveData(Dispatchers.IO){
        emit(Resource.Loding)
    }
}

class LoginScreenViewModelFactory(private val repo: LoginRepo): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginScreenViewModel(repo) as T
    }

}