package app.kawaishiryu.jiujitsu.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.RegisterModelService
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileUserViewModel() : ViewModel() {



    //Obtenemos los datos
    private val _profileUserDbState = MutableStateFlow(CurrentUser.userRegister)
    val profileUserDb: MutableStateFlow<CurrentUser> = _profileUserDbState

    //Creamos la viarable para cerrar sesion
    private var _signOutUserState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    var signOutUserState = _signOutUserState.asStateFlow()


    fun comparationUserDb(uuid: String) = viewModelScope.launch {
        try {
            coroutineScope {
                val userDb  = async{
                    _profileUserDbState.value =  RegisterModelService.dbColletionRefUser(
                        "341421")
                }
                userDb.await()

            }
        } catch (e: Exception) {
            //Cacheo el error

        }
    }

    fun signOutUser() = viewModelScope.launch {

        try {
            coroutineScope {
                val singOut = async {
                    RegisterModelService.signOutUser()
                }
                singOut.await()
                _signOutUserState.value = ViewModelState.SignOutSucces(true)
            }
        }catch (e:Exception){

        }
    }


    //Hacemos un logout
}