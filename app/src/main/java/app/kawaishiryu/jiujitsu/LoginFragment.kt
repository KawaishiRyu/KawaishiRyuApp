package app.kawaishiryu.jiujitsu

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.LoginResult
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.remote.auth.LoginDataSource
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginBinding
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepo
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepoImpl
import app.kawaishiryu.jiujitsu.presentation.auth.LoginScreenViewModel
import app.kawaishiryu.jiujitsu.util.controlEmailAndPassword
import app.kawaishiryu.jiujitsu.view.LocationFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.math.log

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    //Creamos una variable de Firebase Auth y con el metodo by lazy ejecuta tod lo q esta dentro de las llaves
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    //Instanciamos la clase del viewModel
    private val viewModel by viewModels<LoginScreenViewModel>()
    /*

     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        isUserLoggedIn()


        //Esta funcion lo que hace es programar los eventos que se realicen en el textview
        events()
        starFlow()
        intentLocation()
        return binding.root
    }

    private fun starFlow() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loggedInUser.collect(){
                    when(it){
                        is ViewModelState.Logged->{
                            //Tiramos el intent
                            val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(context, "Bienvenido de nuevo :)", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }



        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signInUser.collect(){
                    when(it){
                        is ViewModelState.SignInUserSuccesfully->{
                            //Ingreso Correctamente
                            //Largammos el intent
                            val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
                            startActivity(intent)

                        }
                        is ViewModelState.Error->{
                            //Se producio un error
                        }
                        is ViewModelState.Loading->{
                            //Podemos poner el progressBar
                        }

                    }

                }
            }

        }

    }

    private fun events() = with(binding)
    {

        // Valida el email y la contraseña a medida que vamos ingresando los textos
        // Cuando es valido el email y la contraseña habilitamos el boton
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                btnNavegar.isEnabled = controlEmailAndPassword(
                    editTextEmailId.text.toString(),
                    editTextPasswordId.text.toString()
                )

                txtInpEmail.isErrorEnabled = false
                txtInpPassword.isErrorEnabled = false
            }
        }
        editTextEmailId.addTextChangedListener(textWatcher)
        editTextPasswordId.addTextChangedListener(textWatcher)

        btnNavegar.setOnClickListener {

            signIn(editTextEmailId.text.toString(), editTextPasswordId.text.toString())
        }
        btnRegister.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

    private fun signIn(email: String, password: String) {
        Log.i("email", "$email")
        Log.i("contraseña", "$password")
        viewModel.signIn(CurrentUser(email = email, password = password))

    }

    //Verificamos si existe una cuenta
   private fun isUserLoggedIn() {
        viewModel.userLogged()
        /*
        //Verificamos que el usuario sea distinto de nulo
        firebaseAuth.currentUser?.let {
            //Largamos lo actividad
            /*val intent = Intent(requireContext(),MainMenuHostActivity::class.java)
            startActivity(intent)*/
            val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, "Bienvenido de nuevo :)", Toast.LENGTH_SHORT).show()
        }*/
    }

    //Limpia errores
    fun TextInputLayout.clearError() {
        error = null
        isErrorEnabled = false
    }

    fun intentLocation() {

    }
}