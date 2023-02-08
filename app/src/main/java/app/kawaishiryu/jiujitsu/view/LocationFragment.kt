package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.kawaishiryu.jiujitsu.DojosViewModel
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.Dojos
import app.kawaishiryu.jiujitsu.databinding.FragmentLocationBinding
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginBinding
import app.kawaishiryu.jiujitsu.domain.home.adapeter.LocationDojoAdapter


class LocationFragment : Fragment() {


    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LocationDojoAdapter
    private lateinit var list: ArrayList<Dojos>

    private var selected: Dojos = Dojos()

    private val dojosViewMode: DojosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        intent()

        return binding.root
    }

    fun intent() {
        binding.btnIntentToRegisterDojo.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_registerDojoFragment)
        }
    }
}