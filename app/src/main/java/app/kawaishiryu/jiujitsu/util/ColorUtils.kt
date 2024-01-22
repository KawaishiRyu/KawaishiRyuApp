package app.kawaishiryu.jiujitsu.util

import androidx.core.content.ContextCompat
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailTecBinding
import com.google.android.material.card.MaterialCardView

class ColorUtils {
    companion object {
        private val cinturones = arrayOf(
            "Cinturon blanco", "Cinturon amarillo", "Cinturon naranja",
            "Cinturon verde", "Cinturon azul", "Cinturon marron", "Cinturon negro"
        )

        fun setColorForCardView(binding: FragmentDetailTecBinding, color: String?) {
            color?.let {
                val colorIndex = cinturones.indexOf(it)
                if (colorIndex != -1) {
                    val colorResId = when (colorIndex) {
                        0 -> R.color.C6
                        1 -> R.color.yellow
                        2 -> R.color.orange
                        3 -> R.color.green
                        4 -> R.color.blue
                        5 -> R.color.C3
                        6 -> R.color.black
                        else -> R.color.purple_200 // Color púrpura por defecto
                    }

                    binding.cvDif.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, colorResId))
                } else {
                    // Manejar el caso en que el color no esté en la lista de cinturones
                    binding.cvDif.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.purple_200))
                }
            } ?: run {
                // Manejar el caso en que el color sea nulo
                binding.cvDif.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.purple_200))
            }
        }
    }
}


