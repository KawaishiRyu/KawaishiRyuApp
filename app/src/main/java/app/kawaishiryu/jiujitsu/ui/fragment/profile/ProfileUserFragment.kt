package app.kawaishiryu.jiujitsu.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentProfileUserBinding
import app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.viewmodel.auth.ProfileUserViewModel
import kotlinx.coroutines.launch


class ProfileUserFragment : Fragment(R.layout.fragment_profile_user) {

    private lateinit var binding: FragmentProfileUserBinding
    private val viewModel: ProfileUserViewModel by viewModels()
    private var userModel: UserModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileUserBinding.bind(view)

        binding.btnEditProfileUser.setOnClickListener {
            navigateUserEditProfile(userModel!!)
        }

        val userId = CloudFileStoreWrapper.getCurrentUserId()

        // Asignamos el valor al userModel
        userId?.let { viewModel.getUserDb(it) }?.let { usermodel ->
        }

        startFlow()
    }

    private fun startFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileUserDb.collect() { state ->
                    when (state) {
                        is ViewModelState.Loading2 -> {
                            Log.d("???", "Cargando Usuario")
                        }

                        is ViewModelState.UserLoaded -> {
                            Log.d("???", "Usuario cargado con exito")
                            binding.progressBar.visibility = View.GONE

                            userModel = state.user

                            binding.tvNameUser.text = state.user.name
                            binding.tvApellidoUser.text = state.user.apellido
                            binding.tvEmailUser.text = state.user.email
                            binding.idAcc.text = "ID: ${state.user.id}"
                            binding.tvRol.text = state.user.rol

                        }

                        is ViewModelState.Success2 -> {
                            binding.progressBar.visibility = View.GONE
                            Log.d("???", "Usuario cargado con exito...")
                        }

                        is ViewModelState.Error2 -> {
                            Log.d("???", "Error al cargar Usuario")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun navigateUserEditProfile(user: UserModel) {
        val directions =
            ProfileUserFragmentDirections.actionProfileUserFragmentToProfileUserModifiedFragment(
                user
            )
        findNavController().navigate(directions)
    }
}