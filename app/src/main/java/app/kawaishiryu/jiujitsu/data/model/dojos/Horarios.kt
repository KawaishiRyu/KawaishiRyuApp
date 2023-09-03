package app.kawaishiryu.jiujitsu.data.model.dojos

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

// Agrega la clase Horario aquí
@Parcelize
data class Horarios(
    val dias: List<String> = emptyList(),
    val horarioEntrada: String = "",
    val horarioSalida: String = "",
    val disciplina: String = ""
): Parcelable