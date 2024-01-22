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
import app.kawaishiryu.jiujitsu.databinding.FragmentProfileUserModifiedBinding
import app.kawaishiryu.jiujitsu.util.SnackbarUtils
import app.kawaishiryu.jiujitsu.viewmodel.auth.ProfileUserModifiedViewModel
import kotlinx.coroutines.launch


class ProfileUserModifiedFragment : Fragment(R.layout.fragment_profile_user_modified) {

    private lateinit var binding: FragmentProfileUserModifiedBinding

    //Creamos la variable que contendra los datos del usuario
    private val argsCurrentUser by navArgs<ProfileUserModifiedFragmentArgs>()

    private val viewModel: ProfileUserModifiedViewModel by viewModels()
    private val currentUserModifiedUserModel = UserModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileUserModifiedBinding.bind(view)
        binding.btnChangeDate.setOnClickListener {
            cambiarDatos(view)
        }

        binding.tvId.text = argsCurrentUser.currentUserModifed.id
        binding.tvRol.text = argsCurrentUser.currentUserModifed.rol
        binding.nameAcc.setText(argsCurrentUser.currentUserModifed.name)
        binding.surnameAcc.setText(argsCurrentUser.currentUserModifed.apellido)

        initFlows()
    }

    private fun cambiarDatos(view: View) {
        val nuevoNombre = binding.nameAcc.text.toString()
        val nuevoApellido = binding.surnameAcc.text.toString()

        // Solo actualizamos si el nombre o el apellido son diferentes
        if (nuevoNombre != argsCurrentUser.currentUserModifed.name || nuevoApellido != argsCurrentUser.currentUserModifed.apellido) {
            currentUserModifiedUserModel.id = argsCurrentUser.currentUserModifed.id
            currentUserModifiedUserModel.email = argsCurrentUser.currentUserModifed.email
            currentUserModifiedUserModel.pictureProfile =
                argsCurrentUser.currentUserModifed.pictureProfile
            currentUserModifiedUserModel.apellido = nuevoApellido
            currentUserModifiedUserModel.name = nuevoNombre
            currentUserModifiedUserModel.rol = argsCurrentUser.currentUserModifed.rol

            // Lanzar la operación de modificación
            viewModel.modifiedCurrentUser(
                user = currentUserModifiedUserModel,
                argsCurrentUser.currentUserModifed.id
            )

        } else {
            SnackbarUtils.showCustomSnackbar(
                view,
                layoutInflater,
                3,
                "No se realizaron modificaciones."
            )
        }
    }


    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateUpdateCurrentUserModified.collect() { result ->
                    when (result) {
                        is ViewModelState.Loading2 -> {
                            //Se esta cargando el cambio
                            Log.i("???", "ProfileUserModified: Loading user")
                        }

                        is ViewModelState.UserModifiedSuccesfully -> {
                            //Se realizo con exito el cambio
                            //Modificamos el estado del botton y se cambia de fragmento
                            Log.i("???", "ProfileUserModified: User modified successfully")
                            findNavController().navigate(R.id.action_profileUserModifiedFragment_to_profileUserFragment)
                        }

                        is ViewModelState.Error2 -> {
                            //Se encontro un error
                            Log.i("???", "ProfileUserModified: Error")
                        }

                        is ViewModelState.Empty->{
                            Log.i("???", "ProfileUserModified: Vacio")
                        }
                        else -> {}
                    }

                }
            }
        }
    }

}