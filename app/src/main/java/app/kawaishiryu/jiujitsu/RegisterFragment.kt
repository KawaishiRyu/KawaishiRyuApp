package app.kawaishiryu.jiujitsu

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.kawaishiryu.jiujitsu.core.RegisterViewModel
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.service.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterBinding
import app.kawaishiryu.jiujitsu.util.CamarePermission
import app.kawaishiryu.jiujitsu.util.StoragePermission
import app.kawaishiryu.jiujitsu.util.getRandomUUIDString
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    val currentUserRegister = UserModel()
    private var bitmapeado: Bitmap? = null
    //Creo un nuevo fragmentos
    //tiramos un intent para obtener los valores de la foto
    //Seleccionar imagen
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Si el resultado es correcto esta deberìa ser la selfie
                val data = result.data!!
                bitmapeado = data.extras!!.get("data") as Bitmap
                CurrentUser.userRegister.pictureProfile =
                    binding.btnPictureProfile.setImageBitmap(bitmapeado).toString()
            }
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding =FragmentRegisterBinding.bind(view)


        binding.btnPictureProfile.setOnClickListener {
            permissiones()
        }

        startFlow()
        binding.btnRegister.setOnClickListener {
            //Se creo





            //Falta validaciones
            currentUserRegister.currentUser.email = binding.teEmailUser.text.toString()
            currentUserRegister.currentUser.password = binding.teContraseA.text.toString()
            currentUserRegister.currentUser.name = binding.teNombreDeUsuario.text.toString()
            currentUserRegister.currentUser.apellido= binding.teApellidoUser.text.toString()
            currentUserRegister.currentUser.id = getRandomUUIDString()
            currentUserRegister.currentUser.pictureProfile = bitmapeado.toString()

            Log.i("Useremail", "${ currentUserRegister.currentUser.email}")
            Log.i("foto", "${CurrentUser.userRegister.pictureProfile}")

            viewModel.registrarUsuario(currentUserRegister)
        }


    }


    private fun startFlow() {
        Log.i("registro", "Se Largo la corrutina la corrutina")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.registerUserViewModelState.collect(){
                when(it){
                    is ViewModelState.Loading ->{
                        binding.tvRegistrarse.visibility = View.GONE
                        binding.circularProgressIndicator.visibility = View.VISIBLE
                        binding.tvWaiting.visibility = View.VISIBLE
                    }
                    is ViewModelState.UserRegisterSuccesfully ->{
                        viewModel.profileUserDb.collect(){ userId ->
                            currentUserRegister.currentUser.id = userId
                            viewModel.registerUserCollectionDb(currentUserRegister)
                            baseDeDatosFirebase()
                        }
                    }
                }
            }
            }
        }

    }

    private fun baseDeDatosFirebase() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.registerUserDbViewModelState.collect(){
                    //Los estados posibles de
                    when(it){
                      is  ViewModelState.UserRegisterDbSyccesfully ->{
                          binding.circularProgressIndicator.visibility = View.GONE
                          binding.tvWaiting.visibility = View.GONE
                          binding.tvDone.visibility = View.VISIBLE
                          //Se puso creo bien la base de datos
                          Toast.makeText(context, "Se Pasa a navegacion", Toast.LENGTH_SHORT).show()
                          navigationUp()
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


}