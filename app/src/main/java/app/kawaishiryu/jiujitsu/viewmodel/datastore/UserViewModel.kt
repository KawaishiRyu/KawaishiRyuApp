package app.kawaishiryu.jiujitsu.viewmodel.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.repository.datastore.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userViewState = MutableStateFlow<ViewModelState>(ViewModelState.Empty)
    val userViewState: StateFlow<ViewModelState> = _userViewState

    fun saveUser(name: String, rol: String) = viewModelScope.launch {
        try {
            _userViewState.value = ViewModelState.Loading2("Loading...")
            userRepository.saveUser(name, rol)

            _userViewState.value = ViewModelState.Success2()
        } catch (e: Exception) {
            _userViewState.value = ViewModelState.Error2()
        }
    }

    //    fun getUserProfile() {
//        viewModelScope.launch {
//            userRepository.getUserProfile().collect {
//                _userViewState.value = it
//            }
//        }
//    }

    fun getUserProfile() = viewModelScope.launch {
        try {
            _userViewState.value = ViewModelState.Loading2()

            userRepository.getUserProfile().collect {
                _userViewState.value = it
            }
        } catch (e: Exception) {
            _userViewState.value = ViewModelState.Error2()
        }
    }

}
