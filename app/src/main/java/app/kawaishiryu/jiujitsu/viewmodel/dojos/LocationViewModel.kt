package app.kawaishiryu.jiujitsu.viewmodel.dojos

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.MapsRepository
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService.getListFromFirebase
import app.kawaishiryu.jiujitsu.util.ModelToJson.toHashMap
import app.kawaishiryu.jiujitsu.util.ModelToJson.toJson
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
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

    private val _dojosViewModelState =
        MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val dojosViewModelState: StateFlow<ViewModelState> = _dojosViewModelState

    //Ubicacion
    private val _locationUser = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val locationUser: StateFlow<LatLng> = _locationUser

    fun registerOrUpdate(imageUri: Bitmap?, imageFileName: String, dojoModel: DojosModel, createOrUpdate: Boolean) =
        viewModelScope.launch {

            _dojosViewModelState.value = ViewModelState.Loading

            try {
                if (imageUri != null) {
                    val uploadImage = async {
                        DojosModelService.uploadImageFile(imageUri, imageFileName,
                            DojosModel.DOJOS_IMAGE_FOLDER
                        )
                    }
                    val imageUrl = uploadImage.await() //Obtiene el url de la imagen
                    val imagePath = "${DojosModel.DOJOS_IMAGE_FOLDER}$imageFileName" //Obtiene la ruta donde se guardo la imagen

                    //update image url and image path of the member model
                    dojoModel.dojoUrlImage = imageUrl
                    dojoModel.imagePathUrl = imagePath

                }
                val register = async {
                    dojoModel.toJson()      // Convierte el modelo en JSON
                    val data = dojoModel.toHashMap()// Convierte el modelo en un HashMap con la clave "jsonData"

                    if (createOrUpdate){                                    //Actualiza un dojo
                        DojosModelService.updateDojoFromFirebase(dojoModel,data)
                    }else{                                                  //Crea un dojo
                        DojosModelService.recordWithJson(dojoModel, data)
                    }
                }
                _dojosViewModelState.value = ViewModelState.RegisterSuccessfullyDojo(dojoModel)
                register.await()

            } catch (e: java.lang.Exception) {
                _dojosViewModelState.value = ViewModelState.Error(e.message.toString())
            }
        }

    //Buscar Dojos o Trael la lista de dojos
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
                DojosModelService.deleteDojoFromFirebase(uuid)
                fetchDojosData()             //Actualizar estado con exito
                _deleteDojoState.value = ViewModelState.Succes("Dojo eleminado")
            }catch (e:Exception){
                Log.d("???", "Error")
                _deleteDojoState.value = ViewModelState.Error(e.message ?: "Error al eliminar")
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