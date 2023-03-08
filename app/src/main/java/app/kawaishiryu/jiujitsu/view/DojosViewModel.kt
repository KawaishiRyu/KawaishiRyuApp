package app.kawaishiryu.jiujitsu.view


import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.DojoViewModelState
import app.kawaishiryu.jiujitsu.core.MapsRepository
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DojosViewModel : ViewModel() {

    private val _dojosViewModelState =
        MutableStateFlow<DojoViewModelState>(DojoViewModelState.Empty)
    val dojosViewModelState: StateFlow<DojoViewModelState> = _dojosViewModelState

    fun register(imageUri: Bitmap?, imageFileName: String, dojoModel: DojosModel) =
        viewModelScope.launch {

            _dojosViewModelState.value = DojoViewModelState.Loading

            try {
                if (imageUri != null) {

                    val uploadImage = async {
                        DojosModelService.uploadImageFile(imageUri, imageFileName)
                    }
                    val imageUrl = uploadImage.await() //Obtiene el url de la imagen
                    val imagePath =
                        "${FirebaseStorageManager.DOJOS_IMAGE_FOLDER}$imageFileName" //Obtiene la ruta donde se guardo la imagen

                    //update image url and image path of the member model
                    dojoModel.dojoUrlImage = imageUrl
                    dojoModel.imagePathUrl = imagePath

                }
                val register = async {
                    //Corutina que sube datos
                    DojosModelService.register(dojoModel)
                }
                Log.d("???", "Entro fue exitoso")
                _dojosViewModelState.value = DojoViewModelState.RegisterSuccessfully(dojoModel)
                register.await()

            } catch (e: java.lang.Exception) {
                _dojosViewModelState.value = DojoViewModelState.Error(e.message.toString())
            }
        }


    //Funcion de la ubicacion val dojosViewModelState: StateFlow<DojoViewModelState> = _dojosViewModelState
    private val _locationUser = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val locationUser: StateFlow<LatLng> = _locationUser


    /*
     fun getCurrentLocation() {
        viewModelScope.launch {
            repository.getCurrentLocation()
                .catch { Log.e("MapsViewModel", "Error getting location", it) }
                .collect { _location.value = it }
        }
    }
     */

    //Obtenemos la ubicaion acual del usuario
    fun getUserLocation(context: Context) = viewModelScope.launch {
        try {
            val userLocation = async {
              _locationUser.value = MapsRepository.getCurrentLocation(context)
            }

        } catch (e: Exception) {
            //Tiramos la excepcion
        }
    }

}

