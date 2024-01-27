package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
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
import app.kawaishiryu.jiujitsu.util.GradoDropdownAdapter
import app.kawaishiryu.jiujitsu.util.ListTec
import app.kawaishiryu.jiujitsu.util.SnackbarUtils
import app.kawaishiryu.jiujitsu.viewmodel.tec.CreateTecViewModel
import kotlinx.coroutines.flow.collect
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

        initFlows(view)
        initFlows2(view)

        setupGradoDropdown()

        // CardView elementos principales
        binding.tvTecPrincipal.text = args.mainModelTecArg?.title
        binding.tvTecKanji.text = args.mainModelTecArg?.kanji
        binding.tvTransalte.text = args.mainModelTecArg?.translate

        if (args.editOrCreate) {
            binding.createTec.text = "Guardar cambios"
            binding.tvEditOrCreate.text = "Vas a editar: "

            binding.nameTecCreate.setText(args.movModelArg!!.nameTec)
            binding.nameTranslateTec.setText(args.movModelArg!!.transalteTec)
            binding.urlYouTube.setText(args.movModelArg!!.urlYoutube)
            binding.descripcionTec.setText(args.movModelArg!!.description)
            binding.grado.setText(args.movModelArg!!.grado)
        }

        binding.createTec.setOnClickListener {
            createCollectionTec(view)
        }

    }

    private fun setupGradoDropdown() {
        val adapter = GradoDropdownAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ListTec.cinturones.toList()
        )

        binding.grado.setAdapter(adapter)
        binding.grado.threshold = 1

        // Selecciona la opción que coincide con el valor del otro fragmento
        if (args.movModelArg != null) {
            val selectedValue = args.movModelArg!!.grado
            val position = adapter.getPosition(selectedValue)
            if (position != -1) {
                binding.grado.setText(adapter.getItem(position), false)
            }
        }
    }

    //Obtiene el modelo de movimiento y verifica q los campos no esten vacios
    private fun createCollectionTec(view: View) {
        val movModel = createMovimientosModel()

        when {
            movModel.nameTec.isEmpty() -> showSnackbarError(view, R.string.error_name_required)
            movModel.transalteTec.isEmpty() -> showSnackbarError(
                view,
                R.string.error_translation_required
            )

            movModel.grado.isEmpty() -> showSnackbarError(view, R.string.error_grado_required)
            args.editOrCreate -> {
                viewModel.modifiedCurrentTec(args.mainModelTecArg!!.title, movModel)
            }

            else -> {
                viewModel.register(movModel, args.mainModelTecArg?.title ?: "")
            }
        }
    }

    private fun createMovimientosModel(): MoviemientosModel {

        val movModel = MoviemientosModel()
        movModel.nameTec = binding.nameTecCreate.text.toString()
        movModel.transalteTec = binding.nameTranslateTec.text.toString()
        movModel.urlYoutube = binding.urlYouTube.text.toString()
        movModel.description = binding.descripcionTec.text.toString()
        movModel.grado = binding.grado.text.toString()

        if (args.editOrCreate) {
            movModel.uuId = args.movModelArg!!.uuId
        } else {
            val uuidRandom = getRandomUUIDString()
            movModel.uuId = uuidRandom
        }
        return movModel
    }

    private fun getRandomUUIDString(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun initFlows(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tevViewModelState.collectLatest { state ->
                    when (state) {
                        is ViewModelState.Loading2 -> Log.d("???", "CreateTec Cargando")
                        is ViewModelState.Success2 -> {
                            Log.d("???", "CreateTec Exitoso")
                            SnackbarUtils.showCustomSnackbar(
                                view,
                                layoutInflater,
                                1,
                                getString(R.string.success_register)
                            )
                        }

                        is ViewModelState.Empty -> Log.d("???", "CreateTec Vacio")
                        is ViewModelState.Error -> {
                            Log.d("???", "CreateTec Error: Error")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun initFlows2(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateUpdateCurrentUserModified.collect { state ->
                    // Maneja los diferentes estados aquí
                    Log.d("CreateTecViewModel", "State Update: $state")
                    when (state) {
                        is ViewModelState.Loading2 -> {
                            // Manejar estado de carga
                            Log.d("???", "CreateTecFragment: Loading")
                        }
                        is ViewModelState.TecModifiedSuccesfully -> {
                            // Manejar operación exitosa
                            Log.d("???", "CreateTecFragment: Modified Succes ")
                            SnackbarUtils.showCustomSnackbar(
                                view,
                                layoutInflater,
                                1,
                                getString(R.string.modified_succes)
                            )
                        }
                        is ViewModelState.Empty -> {
                            // Manejar estado vacío
                            Log.d("???", "CreateTecFragment: Vacio")
                        }
                        is ViewModelState.Error -> {
                            // Manejar error
                            Log.d("???", "CreateTecFragment: Error")
                        }
                        // Otros casos...
                        else -> {}
                    }
                }
            }
        }
    }


    private fun showSnackbarError(view: View, @StringRes errorMsgResId: Int) {
        SnackbarUtils.showCustomSnackbar(view, layoutInflater, 2, getString(errorMsgResId))
    }
}
