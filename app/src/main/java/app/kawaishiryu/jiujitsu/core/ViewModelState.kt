package app.kawaishiryu.jiujitsu.core

import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.UserModel

sealed class ViewModelState {

    data class UserRegisterSuccesfully(val user: CurrentUser) : ViewModelState()
    data class SignInUserSuccesfully(val user: CurrentUser) : ViewModelState()
    data class UserRegisterDbSyccesfully(val userModel: UserModel): ViewModelState()
    data class Error(val message: String) : ViewModelState()
    data class Logged(val boolean: Boolean): ViewModelState()

    object Empty : ViewModelState()
    object Loading : ViewModelState()
    object None : ViewModelState()
}