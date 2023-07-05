package app.kawaishiryu.jiujitsu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.kawaishiryu.jiujitsu.databinding.FragmentMenuTecBinding

import app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas.MainAdapterTec
import app.kawaishiryu.jiujitsu.util.ListTec


class MenuTecFragment : Fragment(R.layout.fragment_menu_tec) {

    private lateinit var binding: FragmentMenuTecBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuTecBinding.bind(view)

        binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectios)
    }

}