package app.kawaishiryu.jiujitsu.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenCreated
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.DojoViewModelState
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterDojoBinding
import app.kawaishiryu.jiujitsu.util.StoragePermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RegisterDojoFragment : Fragment(R.layout.fragment_register_dojo) {

    private lateinit var binding: FragmentRegisterDojoBinding
    private val viewModel: DojosViewModel by viewModels()
    private var imageSelectedUri: Uri? = null

    //Seleccionar imagen
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i("imagenes","Llego ahì")
            if (it.resultCode == Activity.RESULT_OK) {
                imageSelectedUri = it.data?.data
                Log.i("imagenes","$imageSelectedUri")
                binding.ivDojosRegFrg.setImageURI(imageSelectedUri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterDojoBinding.bind(view)
        initFlows()
        clickableEvent()
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dojosViewModelState.collect {
                    // Process item
                    when (it) {
                        is DojoViewModelState.Loading -> {
                            //show progresss here
                            Log.d("???", "Cargando")
                            showProgres()
                        }

                        is DojoViewModelState.RegisterSuccessfully -> {
                            hideProgress()
                            //Register succes
                            Log.d("???", "fue exitoso ${it.dojoModel}")
                            Toast.makeText(context, "Register succes", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is DojoViewModelState.Empty -> {
                            //Selected is empty
                            Log.d("???", "Vacio")
                            hideProgress()
                        }
                        is DojoViewModelState.Error -> {
                            Log.d("???", "Error")
                            hideProgress()
                        }

                    }
                }
            }
        }

    }

    private fun clickableEvent() {
        binding.btnCreate.setOnClickListener {
            getAndUploadData()
        }
        binding.ivDojosRegFrg.setOnClickListener {
            solicitarPermisos()
        }
    }

    private fun solicitarPermisos() {
        if (StoragePermission.hasPermission(requireContext())) {
            Toast.makeText(context, "Tiene permisos", Toast.LENGTH_SHORT).show()
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

    private fun getAndUploadData() {

        val modelDojo = DojosModel()
        val uuid = getRandomUUIDString()

        modelDojo.uuId = uuid
        modelDojo.nameSensei = binding.etSenseiName.text.toString().trim()
        modelDojo.nameDojo = binding.etNameDojo.text.toString().trim()
        modelDojo.description = binding.etDescription.text.toString().trim()
        modelDojo.price = binding.etPrice.text.toString().trim()

        viewModel.register(imageSelectedUri, "${modelDojo.uuId}.jpg",modelDojo)
    }

    private fun getRandomUUIDString(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun showProgres(){
        binding.animationFrame.visibility = View.VISIBLE
    }
    private fun hideProgress(){
        binding.animationFrame.visibility = View.GONE
    }

}


