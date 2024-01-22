package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.viewmodel.tec.TecnicasViewModel
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentTecnicasBinding
import app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas.AdapterTec
import app.kawaishiryu.jiujitsu.util.OnItemClick
import app.kawaishiryu.jiujitsu.util.OnItemClickTec
import kotlinx.coroutines.launch


class TecnicasFragment : Fragment(R.layout.fragment_tecnicas), OnItemClickTec {

    private lateinit var binding: FragmentTecnicasBinding
    private val viewModel: TecnicasViewModel by viewModels()

    private val args by navArgs<TecnicasFragmentArgs>()

    private lateinit var adapterTec: AdapterTec

    private var nameFinal: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentTecnicasBinding.bind(view)

        val nameTecnica = args.nameTec
        val subTec = args.nameTecSec
        nameFinal = nameTecnica ?: subTec ?: ""

        setupUI(nameFinal)

        binding.floatingActionButton.setOnClickListener {
            navigateToCreateTecFragment(nameFinal)
        }

        binding.rvTecnicas.layoutManager = LinearLayoutManager(context)

        startFlow()
    }

    private fun setupUI(nameFinal: String) {
        binding.nameTec.text = nameFinal
        binding.tvTransalte.text = args.tanslateTec
        binding.tvDescription.text = getString(args.descriptionTec)
        binding.tvKanji.text = args.kanjiTec
    }

    private fun startFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tecData.observe(viewLifecycleOwner) { data ->
                    updateAdapter(data)

                }
                viewModel.fetchTecData(nameFinal)
            }
        }
    }

    private fun navigateToCreateTecFragment(nameFinal: String) {
        val navController = Navigation.findNavController(requireView())
        val action = TecnicasFragmentDirections.actionTecnicasFragmentToCreateTecFragment(nameFinal)
        navController.navigate(action)
    }

    private fun updateAdapter(data: List<MoviemientosModel>) {
        adapterTec = AdapterTec(data, nameFinal, this@TecnicasFragment)
        binding.rvTecnicas.adapter = adapterTec
    }

    override fun deleteTec(tec: MoviemientosModel) {
        viewModel.deleteDojoFirebase(tec,nameFinal)
    }

    override fun editTec(tec: MoviemientosModel) {
        //Editar tect
    }

}