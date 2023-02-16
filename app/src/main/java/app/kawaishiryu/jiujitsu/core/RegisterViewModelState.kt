package app.kawaishiryu.jiujitsu.core

import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.UserModel

sealed class RegisterViewModelState {
    data class RegisterSuccesfully(val userModel: UserModel) : RegisterViewModelState()
    data class RegisterUserSuccesfully(val userModel: CurrentUser) : RegisterViewModelState()
    data class Error(val message: String): DojoViewModelState()

    object Empty : RegisterViewModelState()
    object Loading : RegisterViewModelState()
    object None : RegisterViewModelState()
}