package app.kawaishiryu.jiujitsu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.service.RegisterTecService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TecnicasViewModel : ViewModel() {

    private val _tecData = MutableLiveData<MutableList<MoviemientosModel>>()
    val tecData: LiveData<MutableList<MoviemientosModel>> = _tecData

    private val _deleteTecState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val deleteDojoState: StateFlow<ViewModelState> = _deleteTecState

    fun fetchTecData(path: String) {
        viewModelScope.launch {
            try {
                val data =
                    RegisterTecService.getListFromFribaseTec(path) // Reemplaza PATH con el valor adecuado
                _tecData.value = data
            } catch (e: Exception) {
                // Manejar la excepción
                Log.d("???", "Error")
            }
        }
    }

    fun deleteDojoFirebase(tec: MoviemientosModel, path: String){
        viewModelScope.launch {
            try {
                //Llamada al metodo de eliminacion con exito
                RegisterTecService.deleteTecFromFirebase(path,tec)
                fetchTecData(path)
                //Actualizar estado con exito
                _deleteTecState.value = ViewModelState.Succes("Dojo eleminado")
            }catch (e:Exception){
                Log.d("???", "Error")
                _deleteTecState.value = ViewModelState.Error(e.message ?: "Error al eliminar")
            }
        }
    }
}