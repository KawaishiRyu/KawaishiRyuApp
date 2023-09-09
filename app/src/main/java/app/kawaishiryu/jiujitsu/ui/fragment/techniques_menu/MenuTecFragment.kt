package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentMenuTecBinding
import app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas.MainAdapterTec
import app.kawaishiryu.jiujitsu.util.ListTec
import app.kawaishiryu.jiujitsu.util.OnItemClick


class MenuTecFragment : Fragment(R.layout.fragment_menu_tec), OnItemClick {

    private lateinit var binding: FragmentMenuTecBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuTecBinding.bind(view)

        binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectionTec, this, false)

        binding.switch3.setOnCheckedChangeListener { _, isCheck ->
            if (isCheck) {
                binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectionTec, this, isCheck)
            } else {
                binding.rvMenuTec.adapter = MainAdapterTec(ListTec.collectionTec, this, isCheck)
            }
        }
    }

    override fun setOnItemClickListener(dojo: DojosModel) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(dojosModel: DojosModel) {
        TODO("Not yet implemented")
    }

    override fun onEditClick(dojosModel: DojosModel) {
        TODO("Not yet implemented")
    }

}