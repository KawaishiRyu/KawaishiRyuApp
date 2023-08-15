package app.kawaishiryu.jiujitsu.core

import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.service.UserModel

sealed class ViewModelState {

    data class UserRegisterSuccesfully(val user: CurrentUser) : ViewModelState()
    data class SignInUserSuccesfully(val user: CurrentUser) : ViewModelState()
    data class UserRegisterDbSyccesfully(val userModel: UserModel): ViewModelState()
    data class UserModifiedSuccesfully(val userModel: UserModel): ViewModelState()

    data class RegisterSuccessfullyDojo(val dojoModel: DojosModel) : ViewModelState()
    data class GetListSuccessfullyDojo(val path: DojosModel) : ViewModelState()

    data class RegisterSuccessfullyMovTec(val movModel: MoviemientosModel): ViewModelState()

    data class Error(val message: String) : ViewModelState()
    data class Logged(val boolean: Boolean): ViewModelState()
    data class SignOutSucces(val boolean: Boolean): ViewModelState()
    data class Succes(val message: String): ViewModelState()

    object Empty : ViewModelState()
    object Loading : ViewModelState()
    object None : ViewModelState()
}