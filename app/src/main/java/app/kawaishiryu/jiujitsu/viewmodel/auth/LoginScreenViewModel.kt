package app.kawaishiryu.jiujitsu.viewmodel.auth

import androidx.lifecycle.*
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.service.RegisterUserModelService
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
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
            val userLogged = async {
                prueba.value = RegisterUserModelService.loggedInUser()
            }
            userLogged.await()
            // Posible errror -------------------------------------------------------------------------------------------
        } catch (e: Exception) {
            _loggedInUser.value = ViewModelState.Error(e.message!!)
        }
    }

    fun signIn(user: UserModel) = viewModelScope.launch {
        //iniciamos el estado en cargando
        _signInUser.value = ViewModelState.Loading

        if (user.email.isNotEmpty() && user.password.isNotEmpty()) {

            try {
                coroutineScope {
                    var usersign = async {
                        //Aqui llamamos el metodo asyncrono para poder utilizar el signIn
                        RegisterUserModelService.signInUser(user)
                    }
                    usersign.await()
                    _signInUser.value = ViewModelState.SignInUserSuccesfully(user)
                }
            } catch (e: Exception) {
                _signInUser.value = ViewModelState.Error(e.message!!)
            }
        }
    }
}