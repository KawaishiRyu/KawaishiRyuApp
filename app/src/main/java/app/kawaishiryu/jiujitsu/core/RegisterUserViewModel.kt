package app.kawaishiryu.jiujitsu.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.data.model.service.UserModelService
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.util.ModelToJson.toHashMap
import app.kawaishiryu.jiujitsu.util.ModelToJson.toJson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Registrar usuario
open class RegisterUserViewModel() : ViewModel() {

    private val _registerUserDbViewModelState =
        MutableStateFlow<ViewModelState>(ViewModelState.None)
    val registerUserDbViewModelState: StateFlow<ViewModelState> = _registerUserDbViewModelState

    private val _registerUserId = MutableStateFlow("")
    val profileUserDb: MutableStateFlow<String> = _registerUserId

    private val _registerUserViewModelState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val registerUserViewModelState: StateFlow<ViewModelState> = _registerUserViewModelState

    private val _userExistenceState = MutableStateFlow(false)
    val userExistenceState: StateFlow<Boolean> = _userExistenceState

    fun registrarUsuario(user: UserModel) = viewModelScope.launch {

        _registerUserViewModelState.value = ViewModelState.Loading2()
        if (user.email.isNotEmpty() && user.password.isNotEmpty()) {
            try {
                coroutineScope {
                    val registerUser = async {
                        _registerUserId.value = UserModelService.registerUser(user)
                    }
                    registerUser.await()
                    _registerUserViewModelState.value = ViewModelState.UserRegisterSuccesfully(user)
                }
            } catch (e: Exception) {
                _registerUserViewModelState.value = ViewModelState.Error2(e.message!!)
                Log.d("???", "se encontro una excepcion: $e")
            }

        } else {
            Log.d("???", "se encontro una excepcion")
        }

    }

    //Registrar
    fun registerUserCollectionDb(user: UserModel) = viewModelScope.launch {
        try {

            coroutineScope {
                val registerUserDb = async {
                    user.toJson()
                    val data = user.toHashMap()
                    UserModelService.registerWithJson(user, data)
                }
                registerUserDb.await()
                _registerUserDbViewModelState.value = ViewModelState.UserRegisterDbSyccesfully(user)
            }

        } catch (e: Exception) {
            _registerUserDbViewModelState.value = ViewModelState.Error(e.message!!)
        }
    }

    // Verifica la existencia del usuario antes de registrar
    suspend fun checkUserExistence(id: String): Boolean {
        return try {
            UserModelService.checkIfUserExists(id)
        } catch (e: Exception) {
            // Manejar la excepción si ocurre algún error al verificar la existencia del usuario
            false
        }
    }
}