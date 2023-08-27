package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentCreateTecBinding
import app.kawaishiryu.jiujitsu.view.CreateTecViewModel
import kotlinx.coroutines.launch
import java.util.*


class CreateTecFragment : Fragment(R.layout.fragment_create_tec) {

    private lateinit var binding: FragmentCreateTecBinding
    private val viewModel: CreateTecViewModel by viewModels()

    private val args by navArgs<CreateTecFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCreateTecBinding.bind(view)

        binding.tvTecPrincipal.text = args.pathFb

        initFlows()

        binding.button5.setOnClickListener {
            createCollectionTec()
        }
    }


    private fun createCollectionTec(){
        val movModel = MoviemientosModel()
        val uuidRandom = getRandomUUIDString()

        movModel.uuId = uuidRandom
        movModel.nameTec = binding.nameTecCreate.text.toString()
        movModel.transalteTec = binding.nombreTraduccionTec.text.toString()
        movModel.urlYoutube = binding.urlYouTube.text.toString()
        movModel.description = binding.descripcionTec.text.toString()
        movModel.grado = binding.grado.text.toString()

        viewModel.register(movModel, args.pathFb)
    }
    private fun getRandomUUIDString(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun initFlows(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.tevViewModelState.collect{
                    when(it){
                        is ViewModelState.Loading->{
                            Log.d("???", "Cargando")
                        }
                        is ViewModelState.RegisterSuccessfullyDojo->{
                            //Register succes
                            Log.d("???", "fue exitoso ${it.dojoModel}")
                            Toast.makeText(context, "Register succes", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is ViewModelState.Empty->{
                            Log.d("???", "Vacio")
                        }
                        is ViewModelState.Error->{
                            Log.d("???", "Error")
                        }
                    }
                }
            }
        }
    }

}