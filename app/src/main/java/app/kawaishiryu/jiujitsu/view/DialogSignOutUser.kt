package app.kawaishiryu.jiujitsu.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.kawaishiryu.jiujitsu.MainActivity
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.databinding.DialogSignoutBinding
import app.kawaishiryu.jiujitsu.presentation.auth.ProfileUserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DialogSignOutUser: DialogFragment() {
    //Instanciamos el binding
    private lateinit var binding: DialogSignoutBinding
    //Creamos el viewmodel
    private val viewModel: ProfileUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSignoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNoSignOut.setOnClickListener {
            //Aqui volveremos a la vista que estaba
            onDestroyView()
        }
        binding.btnYesSignOut.setOnClickListener {
            //aqui llamaremos el metodo signout
            starflow()
        }
    }


    private fun starflow() {
        viewModel.signOutUser()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.prueba3.collect(){
                try {

                    val intent = Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                }catch (e: Exception){
                    //error
                    Log.i("error","$e")
                }
                }
            }
        }
    }

}