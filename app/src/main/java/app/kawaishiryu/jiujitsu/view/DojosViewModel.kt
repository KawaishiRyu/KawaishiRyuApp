package app.kawaishiryu.jiujitsu.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DojoViewModelState {

    data class RegisterSuccessfully(val dojoModel: DojosModel) : DojoViewModelState()
    data class Error(val message: String) : DojoViewModelState()

    object Empty : DojoViewModelState()
    object Loading : DojoViewModelState()
    object None : DojoViewModelState()

}

class DojosViewModel : ViewModel() {
    private val _dojosViewModelState = MutableStateFlow<DojoViewModelState>(DojoViewModelState.None)
    val dojosViewModelState: StateFlow<DojoViewModelState> = _dojosViewModelState

    fun register(dojoModel: DojosModel) = viewModelScope.launch {

        _dojosViewModelState.value = DojoViewModelState.Loading

        try {
            val register = async {
                //Corutina que sube datos
                DojosModelService.register(dojoModel)
            }
            register.await()
            _dojosViewModelState.value = DojoViewModelState.RegisterSuccessfully(dojoModel)
        } catch (e: java.lang.Exception) {
            _dojosViewModelState.value = DojoViewModelState.Error(e.message.toString())
        }
    }

}