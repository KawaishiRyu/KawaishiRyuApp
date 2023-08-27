package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentTimeRegisterBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimeRegisterFragment : Fragment(R.layout.fragment_time_register) {

    private lateinit var binding: FragmentTimeRegisterBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimeRegisterBinding.bind(view)

        var arrayList = ArrayList<String>()

        binding.button2.setOnClickListener {

            arrayList = getSelectedChips(binding.chipGroup)
            binding.textViewPrueba.setText(arrayList.toString())
        }

        binding.button3.setOnClickListener {
            // Crear un listener para el TimePickerDialog
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // Procesar la hora seleccionada
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                // Mostrar la hora seleccionada en un TextView
                binding.textViewH1.text = SimpleDateFormat("HH:mm").format(selectedTime.time)
            }

// Crear y mostrar el TimePickerDialog
            val timePickerDialog = TimePickerDialog(context, timeSetListener, 12, 0, true)
            timePickerDialog.show()
        }
    }

    fun getSelectedChips(chipGroup: ChipGroup): ArrayList<String> {
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