package app.kawaishiryu.jiujitsu.util

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.kawaishiryu.jiujitsu.ui.fragment.location.TimeRegisterFragment
import java.text.SimpleDateFormat
import java.util.Calendar

object ExtraFunctionalities {

    fun showTimePickerAndGetHour(fragment: Fragment, textView: TextView, horaSeleccionada: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            fragment.requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                // Formatear la hora seleccionada como una cadena
                val formattedTime = SimpleDateFormat("HH:mm").format(selectedTime.time)

                // Actualizar el botón correspondiente
                textView.text = formattedTime

                // Llamar a la función de devolución de llamada para pasar la hora seleccionada
                horaSeleccionada(formattedTime)
            },
            currentHour,
            currentMinute,
            true
        )

        timePickerDialog.show()
    }
}
