package app.kawaishiryu.jiujitsu.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.service.UserModelService
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.util.ModelToJson.toHashMap
import app.kawaishiryu.jiujitsu.util.ModelToJson.toJson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileUserModifiedViewModel() : ViewModel() {

    //Creamos el dato que contendra el estado para saber si se realizo bien la modificacion
    private var _stateUpdateCurrentUserModified =
        MutableStateFlow<ViewModelState>(ViewModelState.None)
    val stateUpdateCurrentUserModified = _stateUpdateCurrentUserModified

    fun modifiedCurrentUser(user: UserModel, userId: String) = viewModelScope.launch {
        _stateUpdateCurrentUserModified.value = ViewModelState.Loading2()

        try {
            // Realiza operaciones asíncronas
            val userData = withContext(Dispatchers.Default) {
                // Mantén la conversión a JSON si es necesaria
                user.toJson()

                // Convierte el UserModel a un mapa para la actualización
                user.toHashMap()
            }

            // Llama a la función de servicio para modificar el usuario
            UserModelService.modifiedCurrentUser(user, userId, userData)
            _stateUpdateCurrentUserModified.value = ViewModelState.UserModifiedSuccesfully(user)

        } catch (e: CancellationException) {
            // Ignora las cancelaciones, ya que son esperadas si se destruye el ViewModel
            Log.i("???", "La operación fue cancelada")
            _stateUpdateCurrentUserModified.value = ViewModelState.Error("Operacion cancelada: ${e.message}")
        } catch (e: Exception) {
            // Notifica un estado de error con detalles
            Log.e("???", "Error al modificar el usuario: ${e.message}", e)
            _stateUpdateCurrentUserModified.value = ViewModelState.Error("Error al modificar el usuario: ${e.message}")
        }
    }

}