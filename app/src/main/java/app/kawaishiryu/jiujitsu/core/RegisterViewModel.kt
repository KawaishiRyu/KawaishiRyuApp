package app.kawaishiryu.jiujitsu.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.RegisterModelService
import app.kawaishiryu.jiujitsu.data.model.service.UserModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Member

class RegisterViewModel(): ViewModel() {
    /*
       private val _dojosViewModelState = MutableStateFlow<DojoViewModelState>(DojoViewModelState.None)
    val dojosViewModelState: StateFlow<DojoViewModelState> = _dojosViewModelState
     */
    private val _registerViewModelState = MutableStateFlow<RegisterViewModelState>(RegisterViewModelState.None)
    val registerViewModelState: StateFlow<RegisterViewModelState> = _registerViewModelState

    private val _registerUserViewModelState = MutableStateFlow<RegisterViewModelState>(RegisterViewModelState.None)
    val registerUserViewModelState: StateFlow<RegisterViewModelState> = _registerUserViewModelState

    /*
    auth.createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            // El usuario se registró exitosamente
            val user = auth.currentUser
            // Continúa con el proceso para agregar la foto
        } else {
            // Error en el registro
        }
    }
     */
    fun registrarUsuario(user: CurrentUser)= viewModelScope.launch{


        try {
            Log.i("registrarUsuario","llego hasta aqui")
            coroutineScope {
                val registerUser = async {
                    RegisterModelService.registerUser(user)
                }
                registerUser.await()
                _registerUserViewModelState.value = RegisterViewModelState.RegisterUserSuccesfully(user)

            }

        }catch (e:Exception){
            Log.i("error","se encontro una excepcion")
        }

    }
}