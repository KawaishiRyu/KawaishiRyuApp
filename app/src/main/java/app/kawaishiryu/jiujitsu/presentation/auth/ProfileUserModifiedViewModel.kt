package app.kawaishiryu.jiujitsu.presentation.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.RegisterModelService
import app.kawaishiryu.jiujitsu.data.model.service.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileUserModifiedViewModel(): ViewModel() {

    //Creamos el dato que contendra el estado para saber si se realizo bien la modificacion
    private var _stateUpdateCurrentUserModified = MutableStateFlow<ViewModelState>(ViewModelState.None)
    val stateUpdateCurrentUserModified= _stateUpdateCurrentUserModified

    //funcion que realiza el cambio a la base de datos

    fun modifiedCurrentUser(user: UserModel,userId: String) = viewModelScope.launch {
        //Estado se encuentra cargando
        _stateUpdateCurrentUserModified.value = ViewModelState.Loading
        try {
            //Lanzamos un alcance de corrutina
            coroutineScope {
                //Creamos una variable suspendida
                val userModified = async{
                    //Aqui llamamos al metodo de cambio
                    RegisterModelService.modifiedCurrentUser(user,userId)
                    Log.i("viewModelModifer","entro aqui")
                }
                userModified.await()
                Log.i("viewModelModifer","tira correcto")
                _stateUpdateCurrentUserModified.value = ViewModelState.UserModifiedSuccesfully(user)
            }

        }catch (e:Exception){
            //Si encuentra un error largamos ese estado
            Log.i("viewModelModifer","${e.message}")
            _stateUpdateCurrentUserModified.value = ViewModelState.Error(e.message!!)
        }

    }
}