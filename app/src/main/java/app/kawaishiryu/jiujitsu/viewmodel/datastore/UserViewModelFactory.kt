package app.kawaishiryu.jiujitsu.viewmodel.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.kawaishiryu.jiujitsu.repository.datastore.domain.UserRepository

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
