package app.kawaishiryu.jiujitsu.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.service.UserModelService
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginScreenViewModel() : ViewModel() {

    //Creamos los estados de el mutableFlow
    private var _signInUser = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var signInUser = _signInUser.asStateFlow()

    private var _signInUserWithGoogle = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var signInUserWithGoogle = _signInUserWithGoogle.asStateFlow()

    //var Bool devolver si fue exitoso el inicio de sesion de google
    var loggedBoolGoogle = MutableStateFlow(false)

    private var _loggedInUser = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var loggedInUser = _loggedInUser.asStateFlow()

    //var Bool devolver si esta logeado el usuario
    var loggedBool = MutableStateFlow<Boolean>(false)

    // Método para manejar la lógica de autenticación del usuario
    suspend fun userLogged() {
        try {
            _loggedInUser.value = ViewModelState.Loading2()

            val isUserLogged = withContext(Dispatchers.IO) {
                UserModelService.loggedInUser()
            }

            // Use la información obtenida para determinar el estado
            if (isUserLogged) {
                _loggedInUser.value = ViewModelState.Success2()
            } else {
                _loggedInUser.value = ViewModelState.Empty
            }
        } catch (e: Exception) {
            _loggedInUser.value = ViewModelState.Error2(e.message!!)
        }
    }

    //Inicio de sesion con email
    fun signIn(user: UserModel) = viewModelScope.launch {
        _signInUser.value = ViewModelState.Loading2()

        try {
            val signInResult = withContext(context = Dispatchers.IO) {
                UserModelService.signInUser(user)
            }
            _signInUser.value = ViewModelState.SignInUserSuccesfully(user)
        } catch (e: Exception) {
            _signInUser.value = ViewModelState.Error2()
        }
    }

    // Inicio de session con Google
    fun signInUserWithGoogle(idToken: GoogleSignInAccount?) = viewModelScope.launch {
        _signInUserWithGoogle.value = ViewModelState.Loading2()

        try {
            val (userModel, success) = withContext(Dispatchers.IO) {
                UserModelService.signInWithGoogle(idToken)
            }
            loggedBoolGoogle.value = success

            if (success) {
                _signInUserWithGoogle.value =
                    ViewModelState.SignInUserSuccesfully(userModel)
            } else {
                _signInUserWithGoogle.value =
                    ViewModelState.Error2("Error en el inicio de sesión con Google")
            }

        } catch (e: Exception) {
            loggedBoolGoogle.value = false
            _signInUserWithGoogle.value = ViewModelState.Error(e.message!!)
        }
    }

}