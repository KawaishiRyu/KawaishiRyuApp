package app.kawaishiryu.jiujitsu.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.service.RegisterUserModelService
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.data.model.user.UserModel.Companion.userRegister
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileUserViewModel() : ViewModel() {

    private val _profileUserDbState = MutableStateFlow(userRegister)
    val profileUserDb: MutableStateFlow<UserModel> = _profileUserDbState

    var prueba3  = MutableStateFlow<Boolean>(false)

    //Creamos la viarable para cerrar sesion
    private var _signOutUserState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var signOutUserState = _signOutUserState.asStateFlow()


    fun comparationUserDb(uuid: String) = viewModelScope.launch {
        try {
            coroutineScope {
                val userDb = async {
                    _profileUserDbState.value = RegisterUserModelService.dbColletionRefUser(
                        uuid
                    )
                }
                userDb.await()
            }

        }catch (e:Exception){
        }
    }

    fun signOutUser() = viewModelScope.launch {
        try {
            coroutineScope {
                val singOut = async {
                   prueba3.value =  RegisterUserModelService.signOutUser()
                }
                singOut.await()
            }
        }catch (e:Exception){

        }
    }
}
