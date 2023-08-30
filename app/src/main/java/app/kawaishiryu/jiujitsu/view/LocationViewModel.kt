package app.kawaishiryu.jiujitsu.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService.getListFromFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val _locationViewModelState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val viewModelState: StateFlow<ViewModelState> = _locationViewModelState

    private val _dojosData = MutableLiveData<MutableList<DojosModel>>()
    val dojosData: LiveData<MutableList<DojosModel>> = _dojosData

    private val _deleteDojoState = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val deleteDojoState: StateFlow<ViewModelState> = _deleteDojoState

    fun fetchDojosData() {
        viewModelScope.launch {
            try {
                val data = getListFromFirebase()
                _dojosData.value = data
            } catch (e: Exception) {
                // Manejar la excepción
                Log.d("???", "Error")
            }
        }
    }
    fun deleteDojoFirebase(uuid: DojosModel){
        viewModelScope.launch {
            try {
                //Llamada al metodo de eliminacion con exito
                DojosModelService.deleteDojoFromFirebase(uuid)
                fetchDojosData()
                //Actualizar estado con exito
                _deleteDojoState.value = ViewModelState.Succes("Dojo eleminado")
            }catch (e:Exception){
                Log.d("???", "Error")
                _deleteDojoState.value = ViewModelState.Error(e.message ?: "Error al eliminar")
            }
        }
    }

}