package app.kawaishiryu.jiujitsu.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.service.RegisterTecService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateTecViewModel: ViewModel() {

    private val _tecViewModelState = MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val tevViewModelState: StateFlow<ViewModelState> = _tecViewModelState

    fun register(model: MoviemientosModel, PATH: String) =
        viewModelScope.launch {
            _tecViewModelState.value = ViewModelState.Loading

            try {
                val register = async {
                    RegisterTecService.register(model, PATH)
                }
                _tecViewModelState.value = ViewModelState.RegisterSuccessfullyMovTec(movModel = model)
                register.await()
            }catch (e: java.lang.Exception){
                _tecViewModelState.value = ViewModelState.Error(e.message.toString())
            }
        }
}