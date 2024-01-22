package app.kawaishiryu.jiujitsu.viewmodel.tec

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.service.RegisterTecService
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateTecViewModel: ViewModel() {

    private val _tecViewModelState = MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val tevViewModelState: StateFlow<ViewModelState> = _tecViewModelState

    fun register(model: MoviemientosModel, PATH: String) =
        viewModelScope.launch {
            _tecViewModelState.value = ViewModelState.Loading2()

            try {
                val register = async {

                    //Corutina que sube datos
                    val gson = Gson()
                    val jsonString = gson.toJson(model)
                    val data = hashMapOf("jsonData" to jsonString)

                    RegisterTecService.recordWithJson(PATH, model, data)
                }

                register.await() //Una vez obtenemos la respuesta obtenemos el estado
                //_tecViewModelState.value = ViewModelState.RegisterSuccessfullyMovTec(movModel = model)
                _tecViewModelState.value = ViewModelState.Success2()

            }catch (e: java.lang.Exception){
                _tecViewModelState.value = ViewModelState.Error(e.message.toString())
            }
        }
}