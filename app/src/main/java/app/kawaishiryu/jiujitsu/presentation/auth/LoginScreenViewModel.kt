package app.kawaishiryu.jiujitsu.presentation.auth

import android.view.View
import androidx.lifecycle.*
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.LoginResult
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.RegisterModelService
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepo
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginScreenViewModel() : ViewModel() {

    //Creamos los estados de el mutableFlow

    private var _signInUser = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var signInUser = _signInUser.asStateFlow()

    private var _loggedInUser = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var loggedInUser = _loggedInUser.asStateFlow()

     var prueba = MutableStateFlow<Boolean>(false)


    //Esta funcion nos sirve para cuando el usuario esta logeado
    fun userLogged() = viewModelScope.launch {

        try {
            //------------------------------------------------------------------------------------------------------------
            var userLogged = async {
                prueba.value = RegisterModelService.loggedInUser()
            }
            userLogged.await()
            // Posible errror -------------------------------------------------------------------------------------------

        } catch (e: Exception) {
            _loggedInUser.value = ViewModelState.Error(e.message!!)

        }
    }



    fun signIn(user: CurrentUser) = viewModelScope.launch {
        //iniciamos el estado en cargando
        _signInUser.value = ViewModelState.Loading

        if (user.email.isNotEmpty() && user.password.isNotEmpty()) {

            try {
                coroutineScope {
                    var usersign = async {
                        //Aqui llamamos el metodo asyncrono para poder utilizar el signIn
                        RegisterModelService.signInUser(user)
                    }
                    usersign.await()
                    _signInUser.value = ViewModelState.SignInUserSuccesfully(user)
                }

            } catch (e: Exception) {
                _signInUser.value = ViewModelState.Error(e.message!!)

            }

        }


    }

    /*fun signIn(email: String, password: String): LiveData<LoginResult<FirebaseUser?>> =
        liveData(viewModelScope.coroutineContext + Dispatchers.Main) {

            emit(LoginResult.Loading())

            try {
                emit(LoginResult.Success(repo.sign(email, password)))

            } catch (e: Exception) {

                emit(LoginResult.Failure(e))
            }
        }*/

}

/*class LoginScreenViewModelFactory(private val repo: LoginRepoImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  modelClass.getConstructor(LoginRepoImpl::class.java).newInstance(repo)
    }

}*/

//Se coloca porq no se puede inicializar un viewModel con valores adentro - La instancia del viewModel Siempre tiene q ser vacia
/*class LoginScreenViewModelFactory(private val repo: LoginRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LoginRepo::class.java).newInstance(repo)
    }
}*/