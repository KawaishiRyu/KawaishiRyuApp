package app.kawaishiryu.jiujitsu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.kawaishiryu.jiujitsu.core.RegisterViewModel
import app.kawaishiryu.jiujitsu.core.RegisterViewModelState
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    //Primero obtengo la instancia de Firebase
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        var user1 = CurrentUser()


        binding.btnregistro.setOnClickListener {
            //Se creo
            //Falta validaciones
            user1.email = binding.teEmailUser.text.toString()
            user1.password = binding.teContraseA.text.toString()
            user1.name = binding.teNombreDeUsuario.text.toString()
            user1.apellido = binding.teApellidoUser.text.toString()
            Log.i("Useremail","${user1.email}")
            viewModel.registrarUsuario(user1)
        }
        startFlow()



    }

    private fun startFlow() {
        Log.i("registro","Se Largo la corrutina la corrutina")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.registerUserViewModelState.collect(){
                    //Procesamos el item
                    when(it){
                        is RegisterViewModelState.RegisterUserSuccesfully ->{
                            Toast.makeText(context, "Register succes", Toast.LENGTH_SHORT)
                                .show()
                            Log.i("Registro exitoso","Se registro bien")
                        }

                    }
                }
            }
        }

       /*viewLifecycleOwner.lifecycleScope.launch {
           viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
               viewModel.registerViewModelState.collect(){
                   //Procesamos el Item
                   when(it){
                       is RegisterViewModelState.RegisterSuccesfully ->{
                           //Registro exitoso
                           Toast.makeText(context, "Register succes", Toast.LENGTH_SHORT)
                               .show()
                           Log.i("registro","Se registro")
                       }

                   }
               }
           }
       }*/

    }




}