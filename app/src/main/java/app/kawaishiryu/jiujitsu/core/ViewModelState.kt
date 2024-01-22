package app.kawaishiryu.jiujitsu.core

import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.user.UserModel

sealed class ViewModelState {

    //UserState
    data class UserRegisterSuccesfully(val user: UserModel) : ViewModelState()
    data class SignInUserSuccesfully(val user: UserModel) : ViewModelState()
    data class UserRegisterDbSyccesfully(val userModel: UserModel) : ViewModelState()
    data class UserModifiedSuccesfully(val userModel: UserModel) : ViewModelState()

    data class Error(val message: String) : ViewModelState() //Register Or Update
    data class Logged(val boolean: Boolean) : ViewModelState()
    data class SignOutSucces(val boolean: Boolean) : ViewModelState()
    data class Succes(val message: String) : ViewModelState()

    data object Empty : ViewModelState() //Register or Update
    data object Loading : ViewModelState() //Register or Update
    data object None : ViewModelState()

    data class SignOutSuccess(val message: String = "Cierre de sesión exitoso") : ViewModelState()
    data class SignOutError(val errorMessage: String) : ViewModelState()

    //RegisterDojo
    data class Loading2(val message: String = "Cargando...") : ViewModelState()
    data class Success2(val message: String= "Succes..."): ViewModelState()
    data class Error2(val message: String= "Error..."): ViewModelState()
    data class GetListSuccessfullyDojo(val model: MutableList<DojosModel>) : ViewModelState()

    //User
    data class UserLoaded(val user: UserModel) : ViewModelState()
}