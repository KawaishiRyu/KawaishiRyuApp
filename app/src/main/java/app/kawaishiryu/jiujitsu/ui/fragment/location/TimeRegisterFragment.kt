package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentTimeRegisterBinding
import app.kawaishiryu.jiujitsu.util.ExtraFunctionalities
import app.kawaishiryu.jiujitsu.util.ExtraFunctionalities.showTimePickerAndGetHour
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.collections.ArrayList


class TimeRegisterFragment : Fragment(R.layout.fragment_time_register) {

    private lateinit var binding: FragmentTimeRegisterBinding

    private var arrayList = ArrayList<String>()
    private var horaEntrada: String? = null
    private var horaSalida: String? = null
    private var disciplina: String? = null // También, establece disiplina como nulo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimeRegisterBinding.bind(view)

        binding.btnEntrada.setOnClickListener {
            ExtraFunctionalities.showTimePickerAndGetHour(this, binding.tvEntrada) { formattedTime ->
                horaEntrada = formattedTime
                binding.tvEntrada.text = horaEntrada
            }
        }
        binding.btnSalida.setOnClickListener {
            ExtraFunctionalities.showTimePickerAndGetHour(this, binding.tvSalida) { formattedTime ->
                horaSalida = formattedTime
                binding.tvSalida.text = horaSalida
            }
        }
        binding.button3.setOnClickListener {

            arrayList = getSelectedChips(binding.chipGroup)
            disciplina = binding.etDisiplina.text.toString()
            val h1 = binding.tvEntrada.text.toString()
            val h2 = binding.tvSalida.text.toString()

            binding.textViewH1.text = "$h1 $h2, $arrayList $disciplina"


        }
    }


    private fun getSelectedChips(chipGroup: ChipGroup): ArrayList<String> {
        val selectedChips = ArrayList<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChips.add(chip.text.toString())
            }
        }
        return selectedChips
    }
}