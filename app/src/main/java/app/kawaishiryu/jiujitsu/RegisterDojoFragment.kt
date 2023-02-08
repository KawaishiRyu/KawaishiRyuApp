package app.kawaishiryu.jiujitsu

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import app.kawaishiryu.jiujitsu.data.model.Dojos
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterDojoBinding
import app.kawaishiryu.jiujitsu.util.StoragePermission
import com.google.firebase.storage.FirebaseStorage

class RegisterDojoFragment : Fragment() {

    private var imageSelectedUri: Uri? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageSelectedUri = it.data?.data
                binding.ivDojosRegFrg.setImageURI(imageSelectedUri)
            }
        }

    private var _binding: FragmentRegisterDojoBinding? = null
    private val binding get() = _binding!!
    private var selected: Dojos = Dojos()

    private val dojoViewModel: DojosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterDojoBinding.inflate(inflater, container, false)

        binding.btnCreate.setOnClickListener {
            createNewDojo()
        }
        binding.ivDojosRegFrg.setOnClickListener {
            if (StoragePermission.hasPermission(requireContext())){
                Toast.makeText(context, "Tiene permisos", Toast.LENGTH_SHORT).show()
                val intent =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultLauncher.launch(intent)

            }else{
                StoragePermission.requestPermission(requireContext())
                if (!StoragePermission.shouldShowRequestPermissionRationale(requireContext())){
                    StoragePermission.explainPermission(requireContext())
                }
            }
        }

        return binding.root
    }

    private fun uploadImageFirebase() {
        val storageRef = FirebaseStorage.getInstance().reference.child("image_dojos")

        imageSelectedUri?.let {
            binding.let {
                storageRef.putFile(imageSelectedUri!!)
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener{downloadUrl->
                            Log.d("UrlImage", "la url es: $downloadUrl")
                        }
                    }
            }
        }
    }


    fun createNewDojo() {
        val dojos = Dojos(
            selected.id,
            binding.etSenseiName.text.toString(),
            binding.etNameDojo.text.toString(),
            binding.etDescription.text.toString(),
            binding.etUbication.text.toString()
        )
        if (dojos.id != null) {
            dojoViewModel.update(dojos)
        } else {
            dojoViewModel.create(dojos)
            Toast.makeText(context, "Creado", Toast.LENGTH_SHORT).show()
        }
    }

}

