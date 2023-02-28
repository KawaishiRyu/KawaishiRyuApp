package app.kawaishiryu.jiujitsu.view

import androidx.lifecycle.ViewModel
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class TestViewModelService(){

    data class ReloadSuccesfully(val offset:DocumentSnapshot, val list: List<DojosModel>):TestViewModelService()
    data class LoadMoreSuccessfully(val offset:DocumentSnapshot, val list: List<DojosModel>):TestViewModelService()

    object Empty: TestViewModelService()
    object Loading: TestViewModelService()
    object None: TestViewModelService()

}

class LocationViewModel : ViewModel(){

    private val _viewModelState = MutableStateFlow<TestViewModelService>(TestViewModelService.None)
    val viewModelState : StateFlow<TestViewModelService> = _viewModelState

    fun reload(orderBy: String, limit: Long){

    }

}