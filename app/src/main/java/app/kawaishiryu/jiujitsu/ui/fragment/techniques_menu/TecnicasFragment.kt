package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.data.model.user.UserModel
import app.kawaishiryu.jiujitsu.databinding.FragmentTecnicasBinding
import app.kawaishiryu.jiujitsu.repository.datastore.data.DefaultUserRepository
import app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas.AdapterTec
import app.kawaishiryu.jiujitsu.util.OnItemClickTec
import app.kawaishiryu.jiujitsu.viewmodel.datastore.UserViewModel
import app.kawaishiryu.jiujitsu.viewmodel.datastore.UserViewModelFactory
import app.kawaishiryu.jiujitsu.viewmodel.extfunvm.observeUserViewState
import app.kawaishiryu.jiujitsu.viewmodel.tec.TecnicasViewModel
import kotlinx.coroutines.launch


class TecnicasFragment : Fragment(R.layout.fragment_tecnicas){

    private lateinit var binding: FragmentTecnicasBinding
    private val viewModel: TecnicasViewModel by viewModels()
    private lateinit var viewModel2: UserViewModel

    private val args by navArgs<TecnicasFragmentArgs>()

    private lateinit var adapterTec: AdapterTec
    private var nameFinal: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTecnicasBinding.bind(view)
        setupDataStore()

        nameFinal = if (args.nameTecSec.isNullOrEmpty()) {
            args.nameTec ?: ""
        } else {
            "${args.nameTec ?: ""} ${args.nameTecSec}"
        }

        val mainModelTec = MainModelTec(
            title = nameFinal,
            translate = args.tanslateTec ?: "",
            kanji = args.kanjiTec ?: "",
            description = args.descriptionTec,
        )

        setupUI()

        binding.floatingActionButton.setOnClickListener {
            navigateToCreateTecFragment(mainModelTec)
        }

        binding.rvTecnicas.layoutManager = LinearLayoutManager(context)
        startFlow(mainModelTec)
    }

    //Instancia con viewModel del dataStore
    private fun setupDataStore() {
        val userRepository = DefaultUserRepository(requireContext())
        viewModel2 =
            ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]
        viewModel2.getUserProfile()

        initFlows()
    }

    private fun setupUI() {
        binding.nameTec.text = nameFinal
        binding.tvTransalte.text = args.tanslateTec
        binding.tvDescription.text = getString(args.descriptionTec)
        binding.tvKanji.text = args.kanjiTec
    }

    //Corutina encargada de traer tecnicas Firebase
    private fun startFlow(mainModelTec: MainModelTec) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tecData.observe(viewLifecycleOwner) { data ->
                    updateAdapter(data, mainModelTec)
                }
                viewModel.fetchTecData(nameFinal)
            }
        }
    }

    //Corutina encargada de traer el rol del usuario por DataStore
    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel2.userViewState.collect {
                    handleUserViewState()
                }
            }
        }
    }

    private fun navigateToCreateTecFragment(mainModelTec: MainModelTec) {
        val navController = Navigation.findNavController(requireView())
        val action = TecnicasFragmentDirections.actionTecnicasFragmentToCreateTecFragment(
            false,
            null,
            mainModelTec
        )
        navController.navigate(action)
    }

    private fun updateAdapter(data: List<MoviemientosModel>, mainModelTec: MainModelTec) {
        adapterTec = AdapterTec(
            data,
            movModelMain = mainModelTec
        ) //Realice cambio aqui
        binding.rvTecnicas.adapter = adapterTec
    }

    //Metodo encargado de manejar los estados del stateFlow
    private fun handleUserViewState() {
        viewModel2.observeUserViewState(
            viewLifecycleOwner,
            onLoading = {
                Log.d("???", "TecFragment Datastore: Loading")
                // Show loading state
            },
            onSuccess = { userDataModel ->
                if (userDataModel.rol != UserModel.ROL_ADMIN) {
                    binding.floatingActionButton.visibility = View.VISIBLE
                } else {
                    binding.floatingActionButton.visibility = View.GONE
                }
                Log.d("???", "TecFragment Datastore: Succes, \nUsuario: ${userDataModel.name}" +
                        "\nRol: ${userDataModel.rol}")
            },
            onError = {
                Log.d("???", "TecFragment Datastore: Error")
                // Show error state
            },
            onEmpty = {
                Log.d("???", "TecFragment Datastore: Empty")
            }
        )
    }

}