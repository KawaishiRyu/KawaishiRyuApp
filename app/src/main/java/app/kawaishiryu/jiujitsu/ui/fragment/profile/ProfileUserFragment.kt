package app.kawaishiryu.jiujitsu.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.viewmodel.datastore.UserViewModelFactory
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentProfileUserBinding
import app.kawaishiryu.jiujitsu.repository.datastore.data.DefaultUserRepository
import app.kawaishiryu.jiujitsu.repository.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.viewmodel.auth.ProfileUserViewModel
import app.kawaishiryu.jiujitsu.viewmodel.datastore.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProfileUserFragment : Fragment(R.layout.fragment_profile_user) {

    private lateinit var binding: FragmentProfileUserBinding
    private val viewModel: ProfileUserViewModel by viewModels()
    private var userModel: UserModel? = null

    private lateinit var viewModel2: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileUserBinding.bind(view)
        setupDataStore()
        setupClickListeners()

        val userId = CloudFileStoreWrapper.getCurrentUserId()
        userId?.let { viewModel.getUserDb(it) }?.let {
            startFlow()
        }
    }

    private fun setupDataStore() {
        val userRepository = DefaultUserRepository(requireContext())
        viewModel2 = ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]

        binding.buttonPrueba.setOnClickListener {
            viewModel2.getUserProfile()
        }

        initFlows()
    }

    private fun setupClickListeners() {
        binding.btnEditProfileUser.setOnClickListener {
            navigateUserEditProfile(userModel ?: return@setOnClickListener)
        }
    }

    private fun startFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileUserDb.collect { state ->
                    handleProfileUserState(state)
                }
            }
        }
    }

    private fun handleProfileUserState(state: ViewModelState) {
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

                viewModel2.saveUser(state.user.name, state.user.rol)
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

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel2.userViewState.collect { state ->
                    handleUserViewState(state)
                }
            }
        }
    }

    private fun handleUserViewState(state: ViewModelState) {
        when (state) {
            is ViewModelState.Loading2 -> {
                Log.d("???", "ProfileUserFragmetn: Datastore: Loading")
                // Show loading state
            }

            is ViewModelState.UserDataStoreSuccesfully->{
                Log.d("???","Rol ${state.userDataModel.rol}")
            }

            is ViewModelState.Success2 -> {
                Log.d("???", "ProfileUserFragmetn: Datastore: succes")
            }

            is ViewModelState.Error -> {
                // Show error state
                Log.d("???", "ProfileUserFragmetn: Datastore: Error")
            }

            is ViewModelState.Empty-> {
                Log.d("???", "ProfileUserFragment: Datastore")
            }
            else -> {}
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

