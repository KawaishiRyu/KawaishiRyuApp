package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentLocationBinding


class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var binding : FragmentLocationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLocationBinding.bind(view)
        intent()
    }


    fun intent() {
        binding.btnIntentToRegisterDojo.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_registerDojoFragment)
        }
    }
}