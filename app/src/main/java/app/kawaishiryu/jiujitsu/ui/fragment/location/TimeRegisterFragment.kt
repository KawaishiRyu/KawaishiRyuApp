package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.dojos.Horarios
import app.kawaishiryu.jiujitsu.databinding.FragmentTimeRegisterBinding
import app.kawaishiryu.jiujitsu.util.ExtraFunctionalities.showTimePickerAndGetHour
import app.kawaishiryu.jiujitsu.viewmodel.dojos.LocationViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.collections.ArrayList


class TimeRegisterFragment : Fragment(R.layout.fragment_time_register) {

    private lateinit var binding: FragmentTimeRegisterBinding

    private var arrayListDays = ArrayList<String>()
    private var horaEntrada: String? = null
    private var horaSalida: String? = null
    private var disciplina: String? = null
    private var h1: String? = null
    private var h2: String? = null
    private val listaTemporalHorarios = mutableListOf<Horarios>()


    private val args by navArgs<TimeRegisterFragmentArgs>()
    private val viewModel: LocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimeRegisterBinding.bind(view)

        binding.btnEntrada.setOnClickListener {
            showTimePickerAndGetHour(this, binding.tvEntrada) { formattedTime ->
                horaEntrada = formattedTime
                binding.tvEntrada.text = horaEntrada
            }
        }
        binding.btnSalida.setOnClickListener {
            showTimePickerAndGetHour(this, binding.tvSalida) { formattedTime ->
                horaSalida = formattedTime
                binding.tvSalida.text = horaSalida
            }
        }
        binding.btnAgregarHorario.setOnClickListener {

            arrayListDays = getSelectedChips(binding.chipGroup)
            disciplina = binding.etDisiplina.text.toString()
            h1 = binding.tvEntrada.text.toString()
            h2 = binding.tvSalida.text.toString()

            val nuevoHorario =
                Horarios(
                    disciplina = disciplina!!,
                    horarioEntrada = h1!!,
                    horarioSalida = h2!!,
                    dias = arrayListDays
                )
            // Agregar el nuevo horario a la lista temporal
            listaTemporalHorarios.add(nuevoHorario)

            // Limpiar los campos después de agregar un horario
            binding.etDisiplina.text?.clear()
            binding.tvEntrada.text = "00:00"
            binding.tvSalida.text = "00:00"

            // Formatear la lista de horarios para mostrarla en el TextView
            val listaFormateada = listaTemporalHorarios.joinToString("\n") { horario ->
                val dias = horario.dias.joinToString(", ")
                "Días: $dias\nDisciplina: ${horario.disciplina}\nEntrada: ${horario.horarioEntrada}\nSalida: ${horario.horarioSalida}\n"
            }

            binding.tvList.text = listaFormateada
        }
        binding.btnSubirHorarios.setOnClickListener {
            updateData(arrayListDays, disciplina.toString(), h1.toString(), h2.toString())
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

    //Obtener y actualizar datos
    private fun updateData(
        arrayList: ArrayList<String>,
        disciplina: String,
        h1: String,
        h2: String
    ) {

        val newHorarios = listaTemporalHorarios.map { horario ->
            Horarios(
                disciplina = horario.disciplina,
                horarioEntrada = horario.horarioEntrada,
                horarioSalida = horario.horarioSalida,
                dias = horario.dias
            )
        }

        val newDojo = DojosModel(
            uuId = args.dojoDetail!!.uuId,
            nameSensei = args.dojoDetail!!.nameSensei,
            nameDojo = args.dojoDetail!!.nameDojo,
            dojoUrlImage = args.dojoDetail.dojoUrlImage,
            imagePathUrl = args.dojoDetail.imagePathUrl,
            description = args.dojoDetail.description,
            price = args.dojoDetail!!.price,
            numberWpp = args.dojoDetail!!.numberWpp,
            instaUrl = args.dojoDetail!!.instaUrl,
            facebookUrl = args.dojoDetail!!.facebookUrl,
            latitud = args.dojoDetail!!.latitud,
            longitud = args.dojoDetail!!.longitud,
            horarios = newHorarios.toMutableList()
        )

        viewModel.registerOrUpdate(null, args.dojoDetail.uuId, dojoModel = newDojo, false)
    }
}