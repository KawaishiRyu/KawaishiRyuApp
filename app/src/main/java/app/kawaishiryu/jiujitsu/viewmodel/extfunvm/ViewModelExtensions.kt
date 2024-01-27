package app.kawaishiryu.jiujitsu.viewmodel.extfunvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.datastore.UserModelDataStore
import app.kawaishiryu.jiujitsu.viewmodel.datastore.UserViewModel
import kotlinx.coroutines.launch

fun UserViewModel.observeUserViewState(
    viewLifecycleOwner: LifecycleOwner,
    onLoading: () -> Unit,
    onSuccess: (UserModelDataStore) -> Unit,
    onError: () -> Unit,
    onEmpty: () -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            userViewState.collect { state ->
                when (state) {
                    is ViewModelState.Loading2 -> onLoading()
                    is ViewModelState.UserDataStoreSuccesfully -> onSuccess(state.userDataModel)
                    is ViewModelState.Error -> onError()
                    is ViewModelState.Empty -> onEmpty()
                    else -> {}
                }
            }
        }
    }
}
