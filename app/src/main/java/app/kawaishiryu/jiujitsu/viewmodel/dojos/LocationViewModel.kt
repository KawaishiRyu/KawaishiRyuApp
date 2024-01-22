package app.kawaishiryu.jiujitsu.viewmodel.dojos

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.MapsRepository
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService.getListFromFirebase
import app.kawaishiryu.jiujitsu.data.model.service.ImageService
import app.kawaishiryu.jiujitsu.util.ModelToJson.toHashMap
import app.kawaishiryu.jiujitsu.util.ModelToJson.toJson
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationViewModel : ViewModel() {

    //Get MutableList
    private val _dojosData = MutableLiveData<MutableList<DojosModel>>()
    val dojosData: LiveData<MutableList<DojosModel>> = _dojosData

    //State Register Or Update
    private val _locationViewModelState = MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val locationViewModelState: StateFlow<ViewModelState> = _locationViewModelState

    //Ubicacion
    private val _locationUser = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val locationUser: StateFlow<LatLng> = _locationUser

    fun registerOrUpdate(
        imageUri: Bitmap?,
        imageFileName: String,
        dojoModel: DojosModel,
        createOrUpdate: Boolean
    ) = viewModelScope.launch {
        _locationViewModelState.value = ViewModelState.Loading2()

        runCatching {
            val imageUrl = imageUri?.let {
                withContext(Dispatchers.IO) {
                    ImageService.uploadImageFile(it, imageFileName, DojosModel.DOJOS_IMAGE_FOLDER)
                }
            }
            imageUrl?.let {
                dojoModel.dojoUrlImage = it
                dojoModel.imagePathUrl = "${DojosModel.DOJOS_IMAGE_FOLDER}$imageFileName"
            }
            val data = withContext(Dispatchers.Default) {
                dojoModel.toJson()
                dojoModel.toHashMap()
            }
            if (createOrUpdate) {
                DojosModelService.updateDojoFromFirebase(dojoModel, data)
            } else {
                DojosModelService.registerWithJson(dojoModel, data)
            }
        }.onSuccess {
            _locationViewModelState.value = ViewModelState.Success2()
        }.onFailure { e ->
            _locationViewModelState.value = ViewModelState.Error(e.message.toString())
        }
    }

    //GetListDojo
    fun fetchDojosData() {
        viewModelScope.launch {
            _locationViewModelState.value = ViewModelState.Loading2()
            try {
                val data = getListFromFirebase()
                _dojosData.value = data
                _locationViewModelState.value = ViewModelState.GetListSuccessfullyDojo(data)
            } catch (e: Exception) {
                // Manejar la excepción
                _locationViewModelState.value = ViewModelState.Error2()
            }
        }
    }

    fun deleteDojoFirebase(uuid: DojosModel) {
        viewModelScope.launch {
            _locationViewModelState.value = ViewModelState.Loading2()
            try {
                DojosModelService.deleteDojoFromFirebase(uuid)
                fetchDojosData()             //Actualizar estado con exito
                _locationViewModelState.value = ViewModelState.Success2()
            } catch (e: Exception) {
                _locationViewModelState.value = ViewModelState.Error2(e.message ?: "Error al eliminar")
            }
        }
    }

    //Obtenemos la ubicaion actual del usuario
    fun getUserLocation(context: Context) = viewModelScope.launch {
        try {
            async {
                _locationUser.value = MapsRepository.getCurrentLocation(context)
            }
        } catch (e: Exception) {
            //Tiramos la excepcion
        }
    }
}