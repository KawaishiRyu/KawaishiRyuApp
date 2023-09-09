package app.kawaishiryu.jiujitsu.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.data.model.service.RegisterUserModelService
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Registrar usuario
open class RegisterViewModel() : ViewModel() {

    //Este dato es para Crear la coleccion de base de datos
    private val _registerUserDbViewModelState =
        MutableStateFlow<ViewModelState>(ViewModelState.None)
    val registerUserDbViewModelState: StateFlow<ViewModelState> = _registerUserDbViewModelState

    //Creamos esta variable para poder obtener el valor del UUID del usuario al registarlo
    //Para poder almacenarlo antes de guardarlo en la base de datos
    private val _registerUserId = MutableStateFlow("")
    val profileUserDb: MutableStateFlow<String> = _registerUserId

    private val _registerUserViewModelState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val registerUserViewModelState: StateFlow<ViewModelState> = _registerUserViewModelState


    //Esta funcion retorna una alcance de corrutina en el cual se pasa los parametrso
    fun registrarUsuario(user: UserModel) = viewModelScope.launch {

        _registerUserViewModelState.value = ViewModelState.Loading

        if (user.email.isNotEmpty() && user.password.isNotEmpty()) {

            try {
                coroutineScope {
                    val registerUser = async {
                        _registerUserId.value = RegisterUserModelService.registerUser(user)
                    }
                    registerUser.await()
                    _registerUserViewModelState.value = ViewModelState.UserRegisterSuccesfully(user)
                }

            } catch (e: Exception) {
                _registerUserViewModelState.value = ViewModelState.Error(e.message!!)
                Log.i("error", "se encontro una excepcion")
            }

        } else {
            Log.i("tira vacio", "se encontro una excepcion")
            //Aqui tendriamos que ver si ingreso o no los datos correspondiente
        }

    }

    fun registerUserCollectionDb(userRegisterDbModel: UserModel) = viewModelScope.launch {
        try {
            coroutineScope {
                val registerUserDb = async {
                    //base de datos
                    RegisterUserModelService.register(userRegisterDbModel)
                }
                registerUserDb.await()
                _registerUserDbViewModelState.value = ViewModelState.UserRegisterDbSyccesfully(userRegisterDbModel)
            }

        } catch (e: Exception) {
            _registerUserDbViewModelState.value = ViewModelState.Error(e.message!!)
        }
    }
}