package app.kawaishiryu.jiujitsu.core

import app.kawaishiryu.jiujitsu.data.model.DojosModel


sealed class DojoViewModelState {

    data class RegisterSuccessfully(val dojoModel: DojosModel) : DojoViewModelState()
    data class Error(val message: String) : DojoViewModelState()

    object Empty : DojoViewModelState()
    object Loading : DojoViewModelState()
    object None : DojoViewModelState()
}