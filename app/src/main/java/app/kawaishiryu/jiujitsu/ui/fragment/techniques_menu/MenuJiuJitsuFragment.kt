package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentMenuJiuJitsuBinding
import app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas.MainAdapterTec
import app.kawaishiryu.jiujitsu.util.ListTec
import app.kawaishiryu.jiujitsu.util.OnItemClick


class MenuJiuJitsuFragment : Fragment(R.layout.fragment_menu_jiu_jitsu) {

    private lateinit var binding: FragmentMenuJiuJitsuBinding
    private val nameArt: String = "jiujitsu"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuJiuJitsuBinding.bind(view)

        binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectionTec, false, nameArt)

        binding.switchJiuJitsu.setOnCheckedChangeListener { _, isChecked ->
            setAdapter(isChecked)
        }
    }

    private fun setAdapter(isCheck: Boolean) {
        binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectionTec,  isCheck, nameArt)
    }

}
