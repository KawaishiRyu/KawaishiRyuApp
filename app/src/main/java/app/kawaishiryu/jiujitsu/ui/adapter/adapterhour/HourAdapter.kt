package app.kawaishiryu.jiujitsu.ui.adapter.adapterhour


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.ItemHourBinding
import app.kawaishiryu.jiujitsu.databinding.ItemListLocationBinding
import app.kawaishiryu.jiujitsu.util.OnItemClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class HourAdapter(
    private var horariosFormateados: List<String>,
    private val listener: OnItemClick?
) : RecyclerView.Adapter<HourAdapter.HorariosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorariosViewHolder {
        return HorariosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hour, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HorariosViewHolder, position: Int) {
        val horarioFormateado = horariosFormateados[position]
        holder.render(horarioFormateado)

        holder.view.startAnimation(
            AnimationUtils.loadAnimation(
                holder.view.context,
                R.anim.anim_one
            )
        )
    }

    override fun getItemCount(): Int {
        return horariosFormateados.size
    }

    // View holder encargado de pintar el item del recyclerView
    class HorariosViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemHourBinding.bind(view)

        fun render(horarioFormateado: String) {
            val partes = horarioFormateado.split("\n")

            if (partes.size >= 5) {
                // Asegurarse de que haya suficientes partes antes de intentar establecerlas
                binding.tvHorario.text = partes[0] // La primera parte
                binding.tvDisiplina.text = partes[1] // La segunda parte
                binding.tvDias.text = partes[2] // La tercera parte
                binding.tvHoraEntrada.text = partes[3] // La cuarta parte
                binding.tvHoraSalida.text = partes[4] // La quinta parte
            }
        }
    }

    // Método para actualizar la lista de horarios formateados
    fun setHorarios(horarios: List<String>) {
        horariosFormateados = horarios
        notifyDataSetChanged()
    }
}
