package app.kawaishiryu.jiujitsu.view


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.MapsRepository
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DojosViewModel : ViewModel() {

    private val _dojosViewModelState =
        MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val dojosViewModelState: StateFlow<ViewModelState> = _dojosViewModelState

    fun register(imageUri: Bitmap?, imageFileName: String, dojoModel: DojosModel) =
        viewModelScope.launch {

            _dojosViewModelState.value = ViewModelState.Loading

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
                    val gson = Gson()
                    val jsonString = gson.toJson(dojoModel)
                    val data = hashMapOf("jsonData" to jsonString)

                    DojosModelService.recordWithJson(dojoModel, data)
                }
                Log.d("???", "Entro fue exitoso")
                _dojosViewModelState.value = ViewModelState.RegisterSuccessfullyDojo(dojoModel)
                register.await()

            } catch (e: java.lang.Exception) {
                _dojosViewModelState.value = ViewModelState.Error(e.message.toString())
            }
        }

    fun updateWithBitmap(imageUri: Bitmap?, imageFileName: String, dojoModel: DojosModel) =
        viewModelScope.launch {

            _dojosViewModelState.value = ViewModelState.Loading

            try {
                if (imageUri != null) {

                    val uploadImage = async {
                        DojosModelService.uploadImageFile(imageUri, imageFileName)
                    }
                    val imageUrl = uploadImage.await() //Obtiene el url de la imagen
                    val imagePath =
                        "${FirebaseStorageManager.DOJOS_IMAGE_FOLDER}$imageFileName"

                    //update image url and image path of the member model
                    dojoModel.dojoUrlImage = imageUrl
                    dojoModel.imagePathUrl = imagePath

                }else {
                    Log.d("???", "Error DojoViewModel")
                }
                val updateCorutine = async {

                    //Corutina que sube datos
                    val gson = Gson()
                    val jsonString = gson.toJson(dojoModel)
                    val data = hashMapOf("jsonData" to jsonString)

                    DojosModelService.updateDojoFromFirebase(dojoModel, data)
                }
                Log.d("???", "Entro fue exitoso")
                _dojosViewModelState.value = ViewModelState.RegisterSuccessfullyDojo(dojoModel)
                updateCorutine.await()

            } catch (e: java.lang.Exception) {
                _dojosViewModelState.value = ViewModelState.Error(e.message.toString())
            }
        }

    fun update(dojoModel: DojosModel) =
        viewModelScope.launch {

            _dojosViewModelState.value = ViewModelState.Loading

            try {
                val updateCorutine = async {

                    //Corutina que sube datos
                    val gson = Gson()
                    val jsonString = gson.toJson(dojoModel)
                    val data = hashMapOf("jsonData" to jsonString)

                    DojosModelService.updateDojoFromFirebase(dojoModel, data)
                }
                _dojosViewModelState.value = ViewModelState.RegisterSuccessfullyDojo(dojoModel)
                updateCorutine.await()
            } catch (e: java.lang.Exception) {
                _dojosViewModelState.value = ViewModelState.Error(e.message.toString())
            }
        }


    //Ubicacion
    private val _locationUser = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val locationUser: StateFlow<LatLng> = _locationUser

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

