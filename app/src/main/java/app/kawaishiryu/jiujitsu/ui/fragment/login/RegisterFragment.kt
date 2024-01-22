package app.kawaishiryu.jiujitsu.ui.fragment.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.kawaishiryu.jiujitsu.ui.MainMenuHostActivity
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.RegisterUserViewModel
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterBinding
import app.kawaishiryu.jiujitsu.util.ImageManipulationUtil
import app.kawaishiryu.jiujitsu.util.permission.StoragePermission
import app.kawaishiryu.jiujitsu.util.getRandomUUIDString
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterUserViewModel by viewModels()
    private val currentUserRegister = UserModel()

    private var imageSelectedUri: Uri? = null

    //Seleccionar imagen ELIMINADO
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageSelectedUri = it.data?.data
                //binding.ivUser.setImageURI(imageSelectedUri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        //SAQUE LA IMAGEN DE USUARIO
//        binding.ivUser.setOnClickListener {
//            permissiones()
//        }

        startFlow()
    }

    private fun registerUser() {
        currentUserRegister.name = binding.etNombre.text.toString().trim()
        currentUserRegister.apellido = binding.etApellido.text.toString().trim()
        currentUserRegister.email = binding.etEmail.text.toString().trim()
        currentUserRegister.password = binding.etPassword.text.toString().trim()
        currentUserRegister.id = getRandomUUIDString()
        currentUserRegister.rol = "Alumno"

        val compressedBitmap = imageSelectedUri?.let { ImageManipulationUtil.compressBitmap(it, requireContext(), quality = 10) }

        viewModel.registrarUsuario(currentUserRegister)
    }


    private fun startFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUserViewModelState.collect() {
                    when (it) {
                        is ViewModelState.Loading -> {
                            Log.d("???","RegisterFragment: Registering user please wait")
                            binding.tvRegistrarse.visibility = View.GONE
                            binding.circularProgressIndicator.visibility = View.VISIBLE
                            binding.tvWaiting.visibility = View.VISIBLE
                        }

                        is ViewModelState.UserRegisterSuccesfully -> {
                            viewModel.profileUserDb.collect() { userId ->
                                Log.d("???","RegisterFragment: Registered user successfully")

                                currentUserRegister.id = userId
                                viewModel.registerUserCollectionDb(currentUserRegister)

                                binding.circularProgressIndicator.visibility = View.GONE
                                binding.tvWaiting.visibility = View.GONE
                                binding.tvDone.visibility = View.VISIBLE
                                navigationUp()
                            }
                        }

                        is ViewModelState.Error -> { it
                            Log.d("???","RegisterFragment: Error-> $it")
                        }
                        else -> {}
                    }
                }
            }
        }
    }


    private fun permissiones() {
        if (StoragePermission.hasPermission(requireContext())) {
            //Tiene permisos
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)

        } else {
            StoragePermission.requestPermission(requireContext())
            if (!StoragePermission.shouldShowRequestPermissionRationale(requireContext())) {
                StoragePermission.explainPermission(requireContext())
            }
        }
    }

    private fun navigationUp() {
        val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
        startActivity(intent)
    }

}