package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import app.kawaishiryu.jiujitsu.data.model.service.DojosModelService
import app.kawaishiryu.jiujitsu.databinding.FragmentLocationBinding
import app.kawaishiryu.jiujitsu.ui.adapter.DojosAdapter
import app.kawaishiryu.jiujitsu.util.OnItemClick
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationFragment : Fragment(R.layout.fragment_location), OnItemClick {

    private lateinit var binding: FragmentLocationBinding
    private var db = Firebase.firestore
    private lateinit var adapterDojo: DojosAdapter

    private lateinit var newArrayList: ArrayList<DojosModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        binding = FragmentLocationBinding.bind(view)
        intent()

        binding.rvLocation.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val data = DojosModelService.getListFromFirebase()

            for (i in data) {
                Log.d("Lista: ", "\n$i")
            }

            withContext(Dispatchers.Main) {
                adapterDojo = DojosAdapter(data, this@LocationFragment)
                binding.rvLocation.adapter = adapterDojo
            }
        }

    }

    fun intent() {
        binding.btnIntentToRegisterDojo.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_registerDojoFragment)
        }
    }

    override fun setOnItemClickListener(dojo: DojosModel) {
        val directions = LocationFragmentDirections.actionLocationFragmentToLocationDojoFragment(dojo)
        findNavController().navigate(directions)
    }
}