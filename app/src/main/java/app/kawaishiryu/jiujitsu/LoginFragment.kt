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
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.kawaishiryu.jiujitsu.data.LoginResult
import app.kawaishiryu.jiujitsu.data.remote.auth.LoginDataSource
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginBinding
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepo
import app.kawaishiryu.jiujitsu.domain.auth.LoginRepoImpl
import app.kawaishiryu.jiujitsu.presentation.auth.LoginScreenViewModel
import app.kawaishiryu.jiujitsu.presentation.auth.LoginScreenViewModelFactory
import app.kawaishiryu.jiujitsu.util.controlEmailAndPassword
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Pattern
import kotlin.math.log

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //Creamos una variable de Firebase Auth y con el metodo by lazy ejecuta tod lo q esta dentro de las llaves
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }



    //Instanciamos la clase del viewModel
    private val viewModel by viewModels<LoginScreenViewModel> {
        LoginScreenViewModelFactory(LoginRepoImpl(LoginDataSource()))
    }

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
        return binding.root
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
            signIn(editTextEmailId.text.toString(),editTextPasswordId.text.toString())
        }


    }

    private fun signIn(email: String, password: String) {
        Log.i("email","$email")
        Log.i("contraseña","$password")
        viewModel.signIn(email, password)
            .observe(viewLifecycleOwner, Observer { result ->

                when (result) {
                    is LoginResult.Loading -> {

                    }
                    is LoginResult.Success -> {

                        val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
                        startActivity(intent)

                        Toast.makeText(
                            requireContext(),
                            "Bienvenido",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    is LoginResult.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Usuario NO encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })

    }

    //Verificamos si existe una cuenta
    private fun isUserLoggedIn() {
        //Verificamos que el usuario sea distinto de nulo
        firebaseAuth.currentUser?.let {
            //Largamos lo actividad
            /*val intent = Intent(requireContext(),MainMenuHostActivity::class.java)
            startActivity(intent)*/
            val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, "Bienvenido de nuevo :)", Toast.LENGTH_SHORT).show()
        }
    }

    //Limpia errores
    fun TextInputLayout.clearError() {
        error = null
        isErrorEnabled = false
    }
}