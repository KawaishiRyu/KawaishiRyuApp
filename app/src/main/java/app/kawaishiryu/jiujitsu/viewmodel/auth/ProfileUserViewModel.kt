package app.kawaishiryu.jiujitsu.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.service.UserModelService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileUserViewModel() : ViewModel() {

    private val _profileUserDbState = MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val profileUserDb: StateFlow<ViewModelState> = _profileUserDbState

    val _signOutUserState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val signOutUserState: StateFlow<ViewModelState> = _signOutUserState

    fun getUserDb(uuid: String) = viewModelScope.launch {
        try {
            _profileUserDbState.value = ViewModelState.Loading2("Cargando usuario...")

            val user = UserModelService.getUserFromFirebaseById(uuid)

            _profileUserDbState.value = ViewModelState.UserLoaded(user)

        } catch (e: Exception) {
            _profileUserDbState.value = ViewModelState.Error("Error al obtener usuario: ${e.message}")
        }
    }

    fun signOutUser() = viewModelScope.launch {
        try {
            val success = UserModelService.signOutUser()
            _signOutUserState.value = ViewModelState.SignOutSucces(true)
        } catch (e: Exception) {
            _signOutUserState.value = ViewModelState.Error("Error al cerrar sesión: ${e.message}")
        }
    }
}
