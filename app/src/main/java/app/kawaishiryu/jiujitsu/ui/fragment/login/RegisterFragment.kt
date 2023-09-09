package app.kawaishiryu.jiujitsu.ui.fragment.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import app.kawaishiryu.jiujitsu.core.RegisterViewModel
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterBinding
import app.kawaishiryu.jiujitsu.util.CamarePermission
import app.kawaishiryu.jiujitsu.util.getRandomUUIDString
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val currentUserRegister = UserModel()

    private var imageSelectedUri: Uri? = null

    //Seleccionar imagen
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageSelectedUri = it.data?.data
                binding.ivUser.setImageURI(imageSelectedUri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.ivUser.setOnClickListener {
            permissiones()
        }

        startFlow()
    }

    private fun registerUser() {
        currentUserRegister.name = binding.etNombre.text.toString().trim()
        currentUserRegister.apellido = binding.etApellido.text.toString().trim()
        currentUserRegister.email = binding.etEmail.text.toString().trim()
        currentUserRegister.password = binding.etPassword.text.toString().trim()
        currentUserRegister.id = getRandomUUIDString()

        val bitmap = getBitmapFromUri(imageSelectedUri!!, requireContext(), quality = 10)


        viewModel.registrarUsuario(currentUserRegister)
    }


    private fun startFlow() {
        Log.i("registro", "Se Largo la corrutina la corrutina")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUserViewModelState.collect() {
                    when (it) {
                        is ViewModelState.Loading -> {
                            binding.tvRegistrarse.visibility = View.GONE
                            binding.circularProgressIndicator.visibility = View.VISIBLE
                            binding.tvWaiting.visibility = View.VISIBLE
                        }

                        is ViewModelState.UserRegisterSuccesfully -> {
                            viewModel.profileUserDb.collect() { userId ->

                                currentUserRegister.id = userId
                                viewModel.registerUserCollectionDb(currentUserRegister)

                                binding.circularProgressIndicator.visibility = View.GONE
                                binding.tvWaiting.visibility = View.GONE
                                binding.tvDone.visibility = View.VISIBLE
                                navigationUp()
                            }
                        }

                        is ViewModelState.Error -> { it
                            Log.d("???", "$it")
                            Toast.makeText(context, "Error $it", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun permissiones() {
        if (CamarePermission.hasPermission(requireContext())) {
            Toast.makeText(context, "Tiene permisos", Toast.LENGTH_SHORT).show()
            val intent =
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(intent)

        } else {
            CamarePermission.requestPermission(requireContext())
            if (!CamarePermission.shouldShowRequestPermissionRationale(requireContext())) {
                CamarePermission.explainPermission(requireContext())
            }
        }

    }


    private fun navigationUp() {
        val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
        startActivity(intent)
    }

    //    BitMapFromUri
    private fun getBitmapFromUri(uri: Uri, context: Context, quality: Int = 100): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeStream(inputStream, null, options)?.let { bitmap ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                BitmapFactory.decodeByteArray(
                    outputStream.toByteArray(),
                    0,
                    outputStream.toByteArray().size
                )
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}