package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentMenuJudoBinding
import app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas.MainAdapterTec
import app.kawaishiryu.jiujitsu.util.ListTec
import app.kawaishiryu.jiujitsu.util.OnItemClick


class MenuJudoFragment : Fragment(R.layout.fragment_menu_judo) {

    private lateinit var binding: FragmentMenuJudoBinding
    private val nameArt: String = "judo"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuJudoBinding.bind(view)

        binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectionTecJudo, false, nameArt)

        binding.switchJudo.setOnCheckedChangeListener { _, isChecked ->
            setAdapter(isChecked)
        }
    }

    private fun setAdapter(isCheck: Boolean) {
        binding.rvMenuTec.adapter =
            MainAdapterTec(ListTec.collectionTecJudo, isCheck, nameArt)
    }
}

