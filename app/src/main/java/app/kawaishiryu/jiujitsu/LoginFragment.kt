package app.kawaishiryu.jiujitsu

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.PatternsCompat
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //Creamos una variable de Firebase Auth y con el metodo by lazy ejecuta tod lo q esta dentro de las llaves
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        //Inicializacion
        isUserLoggedIn()

        binding.btnNavegar.setOnClickListener {
            valideCredential()
        }
        return binding.root
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
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "El usuario es nulo", Toast.LENGTH_SHORT).show()
    }

    private fun valideCredential() {
        val result = arrayOf(valideteEmail() , validePassword())

        if(false in result){
            return
        }

        Toast.makeText(context, "El usuario ingreso", Toast.LENGTH_SHORT).show()
    }

    private fun valideteEmail(): Boolean {

        val email = binding.txtInpEmail.editText?.text.toString().trim()

        return if (email.isEmpty()) {
            binding.editTextEmailId.error = "No puede estar vacio"
            false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmailId.error = "Por favor ingrese un email valido"
            false
        } else {
            binding.txtInpEmail.error = null
            Toast.makeText(context, "Good", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun validePassword(): Boolean {

        val password = binding.txtInpPassword.editText?.text.toString().trim()

        val passwordRegex = Pattern.compile(
            ".{4,}"// Minimo cuatro caracteres
        )

        return if (password.isEmpty()){
            binding.txtInpPassword.error = "El archivo no puede estar vacio"
            false
        }else if (!passwordRegex.matcher(password).matches()){
            binding.txtInpPassword.error = "La contraseña es muy corta"
            false
        }else{
            binding.editTextEmailId.error = null
            binding.txtInpPassword.clearError()
            true
        }
    }

    //Limpia errores
    fun TextInputLayout.clearError() {
        error = null
        isErrorEnabled = false
    }
}