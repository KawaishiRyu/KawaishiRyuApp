package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentLocationBinding
import app.kawaishiryu.jiujitsu.ui.adapter.dojo_adap.DojosAdapter
import app.kawaishiryu.jiujitsu.util.OnItemClick
import app.kawaishiryu.jiujitsu.viewmodel.dojos.LocationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class LocationFragment : Fragment(R.layout.fragment_location), OnItemClick {

    private lateinit var binding: FragmentLocationBinding
    private val viewModel: LocationViewModel by viewModels()
    private var db = Firebase.firestore

    private lateinit var adapterDojo: DojosAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        binding = FragmentLocationBinding.bind(view)
        intent()

        binding.rvLocation.layoutManager = LinearLayoutManager(context)

        startFlow()
    }

    fun intent() {
        binding.btnIntentToRegisterDojo.setOnClickListener {
            val directions =
                LocationFragmentDirections.actionLocationFragmentToRegisterDojoFragment()
            findNavController().navigate(directions)
        }
    }

    override fun setOnItemClickListener(dojo: DojosModel) {
        val directions =
            LocationFragmentDirections.actionLocationFragmentToDetailLocationFragment(dojo)
        findNavController().navigate(directions)
    }


    override fun onDeleteClick(dojosModel: DojosModel) {
        viewModel.deleteDojoFirebase(dojosModel)
        Toast.makeText(context, "Hola delete", Toast.LENGTH_SHORT).show()
    }

    override fun onEditClick(dojosModel: DojosModel) {
        val directions =
            LocationFragmentDirections.actionLocationFragmentToRegisterDojoFragment(dojosModel)
        findNavController().navigate(directions)
    }


    private fun startFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dojosData.observe(viewLifecycleOwner) { data ->
                    Log.d("???", "$data")
                    adapterDojo = DojosAdapter(data, this@LocationFragment)
                    binding.rvLocation.adapter = adapterDojo
                }
                viewModel.fetchDojosData()
            }
        }
    }
}


