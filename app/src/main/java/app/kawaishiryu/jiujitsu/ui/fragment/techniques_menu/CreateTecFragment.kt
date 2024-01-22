package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentCreateTecBinding
import app.kawaishiryu.jiujitsu.util.ListTec
import app.kawaishiryu.jiujitsu.util.SnackbarUtils
import app.kawaishiryu.jiujitsu.viewmodel.tec.CreateTecViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID


class CreateTecFragment : Fragment(R.layout.fragment_create_tec) {

    private lateinit var binding: FragmentCreateTecBinding

    private val viewModel: CreateTecViewModel by viewModels()

    private val args by navArgs<CreateTecFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCreateTecBinding.bind(view)

        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                ListTec.cinturones
            )

        binding.grado.setAdapter(adapter)
        binding.grado.threshold = 1

        binding.tvTecPrincipal.text = args.pathFb

        binding.createTec.setOnClickListener {
            createCollectionTec(view)
        }

        initFlows(view)
    }


    private fun createCollectionTec(view: View) {
        val movModel = MoviemientosModel()
        val uuidRandom = getRandomUUIDString()

        movModel.uuId = uuidRandom
        movModel.nameTec = binding.nameTecCreate.text.toString()
        movModel.transalteTec = binding.nombreTraduccionTec.text.toString()
        movModel.urlYoutube = binding.urlYouTube.text.toString()
        movModel.description = binding.descripcionTec.text.toString()
        movModel.grado = binding.grado.text.toString()

        // Verifica si los campos obligatorios están vacíos
        when {
            movModel.nameTec.isEmpty() -> SnackbarUtils.showCustomSnackbar(
                view,
                layoutInflater,
                2,
                "Debes colocar un nombre en tecnica"
            )

            movModel.transalteTec.isEmpty() -> SnackbarUtils.showCustomSnackbar(
                view,
                layoutInflater,
                2,
                "Debes completar el campo de traducción"
            )

            movModel.grado.isEmpty() -> SnackbarUtils.showCustomSnackbar(
                view,
                layoutInflater,
                2,
                "Debes seleccionar una opción en grado de dificultad"
            )

            else -> {
                // Todos los campos obligatorios están completos, llama a la función register
                viewModel.register(movModel, args.pathFb)
            }
        }

    }

    private fun getRandomUUIDString(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun initFlows(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tevViewModelState.collectLatest { state ->
                    when (state) {
                        is ViewModelState.Loading2 -> {
                            Log.d("???", "CreateTec Cargando")
                        }

                        is ViewModelState.Success2 -> {
                            Log.d("???", "CreateTec Exitoso")

                            val message: String = "Registro exitoso."

                            SnackbarUtils.showCustomSnackbar(
                                view,
                                layoutInflater,
                                1,
                                message
                            )
                        }

                        is ViewModelState.Empty -> {
                            Log.d("???", "CreateTec Vacio")
                        }

                        is ViewModelState.Error -> {
                            Log.d("???", "CreateTec Error")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

}