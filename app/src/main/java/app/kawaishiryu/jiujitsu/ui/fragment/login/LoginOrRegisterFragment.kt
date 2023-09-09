package app.kawaishiryu.jiujitsu.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import app.kawaishiryu.jiujitsu.ui.MainMenuHostActivity
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginOrRegisterBinding
import app.kawaishiryu.jiujitsu.viewmodel.auth.LoginScreenViewModel
import app.kawaishiryu.jiujitsu.util.controlEmailAndPassword
import kotlinx.coroutines.launch


class LoginOrRegisterFragment : Fragment(R.layout.fragment_login_or_register) {

    private lateinit var binding: FragmentLoginOrRegisterBinding

    //Instanciamos la clase del viewModel
    private val viewModel by viewModels<LoginScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginOrRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            navigationRegister()
        }
        binding.btnLogin.setOnClickListener {
            navigationSignIn()
        }

        //Esta funcion lo que hace es programar los eventos que se realicen en el textview
        events()
        starFlow()
    }

    private fun navigationSignIn() {
    }

    private fun navigationRegister() {
        requireView().findNavController().navigate(R.id.action_loginOrRegisterFragment_to_registerFragment)
    }


    private fun starFlow() {
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
                btnLogin.isEnabled = controlEmailAndPassword(
                    etUsuario.text.toString(),
                    etPassword.text.toString()
                )

                tvInpLUsuario.isErrorEnabled = false
                tvInpLPassword.isErrorEnabled = false
            }
        }
        etUsuario.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)

        btnLogin.setOnClickListener {

            signIn(etUsuario.text.toString(), etPassword.text.toString())
        }
        btnRegister.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_loginOrRegisterFragment_to_registerFragment)
        }


    }

    private fun signIn(email: String, password: String) {
        Log.i("email", "$email")
        Log.i("contraseña", "$password")
        viewModel.signIn(UserModel(email = email, password = password))

    }

}