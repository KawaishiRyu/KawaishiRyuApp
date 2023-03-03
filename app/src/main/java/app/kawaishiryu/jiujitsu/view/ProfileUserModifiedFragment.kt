package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.service.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentProfileUserBinding
import app.kawaishiryu.jiujitsu.databinding.FragmentProfileUserModifiedBinding
import app.kawaishiryu.jiujitsu.presentation.auth.ProfileUserModifiedViewModel
import app.kawaishiryu.jiujitsu.presentation.auth.ProfileUserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ProfileUserModifiedFragment : Fragment(R.layout.fragment_profile_user_modified) {
    // TODO: Rename and change types of parameters

    //Creamos la vinculacion de datos
    private lateinit var binding: FragmentProfileUserModifiedBinding
    //Creamos la variable que contendra los datos del usuario
    private val argsCurrentUser by navArgs<ProfileUserModifiedFragmentArgs>()
    //Creamos el viewmodels
    private val viewModel: ProfileUserModifiedViewModel by viewModels()
    //Instanciamos la clase que modificaremos
    private val currentUserModifiedUserModel  = UserModel()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Ya inicio","Estamos aqui")
        binding =  FragmentProfileUserModifiedBinding.bind(view)
        binding.btnChangeDate.setOnClickListener {
            cambiarDatos()
        }
    }

    private fun cambiarDatos() {
        Log.i("datos ID Actuales modifides","${argsCurrentUser.currentUserModifed.id}")
        Log.i("datos1","${argsCurrentUser.currentUserModifed.password}")
        Log.i("datos1","${argsCurrentUser.currentUserModifed.name}")
        Log.i("datos1","${argsCurrentUser.currentUserModifed.email}")
        //boton cuando se realice un evento
        //llamomos a la id random a traves de coso
        currentUserModifiedUserModel.currentUser.id= argsCurrentUser.currentUserModifed.id
        currentUserModifiedUserModel.currentUser.email = argsCurrentUser.currentUserModifed.email
        currentUserModifiedUserModel.currentUser.pictureProfile = argsCurrentUser.currentUserModifed.pictureProfile
        currentUserModifiedUserModel.currentUser.apellido = argsCurrentUser.currentUserModifed.apellido
        currentUserModifiedUserModel .currentUser.name = "Nacho Puto"

        Log.i("datos2","${currentUserModifiedUserModel.currentUser.name}")
        Log.i("datos2","${currentUserModifiedUserModel.currentUser.email}")

        viewModel.modifiedCurrentUser(user = currentUserModifiedUserModel ,argsCurrentUser.currentUserModifed.id)
        //Le enviamos a la base de datos
        initFlows()


    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.stateUpdateCurrentUserModified.collect(){result ->
                    when(result){
                        is ViewModelState.Loading->{
                            //Se esta cargando el cambio
                            Log.i("modificacion","Esta aqui")

                        }
                        is ViewModelState.UserModifiedSuccesfully-> {
                            //Se realizo con exito el cambio
                            //Modificamos el estado del botton y se cambia de fragmento
                            Log.i("acepto","Aqui")
                            findNavController().navigate(R.id.action_profileUserModifiedFragment_to_profileUserFragment)

                        }
                        is ViewModelState.Error->{
                            //Se encontro un error
                            Log.i("Error","error")
                            //Mostraremos el erro ?
                        }
                    }

                }
            }
        }
    }


}