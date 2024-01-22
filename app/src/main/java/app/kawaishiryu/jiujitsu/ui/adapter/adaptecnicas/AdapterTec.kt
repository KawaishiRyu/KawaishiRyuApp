package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.databinding.ItemTechinqueBinding
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.TecnicasFragmentDirections
import app.kawaishiryu.jiujitsu.util.OnItemClickTec

class AdapterTec(
    private val list: List<MoviemientosModel>,
    private val nameFinal: String,
    private val listener: OnItemClickTec
) : RecyclerView.Adapter<AdapterTec.TecViewHolder>() {

    inner class TecViewHolder(private val binding: ItemTechinqueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MoviemientosModel) {
            binding.apply {
                tvTecnica.text = model.nameTec

                itemView.setOnClickListener {
                    val navController = Navigation.findNavController(itemView)
                    val action =
                        TecnicasFragmentDirections.actionTecnicasFragmentToDetailTecFragment(
                            model,
                            nameFinal
                        )
                    navController.navigate(action)
                }

                imgDeleteTec.setOnClickListener {
                    listener.deleteTec(model)
                }

                imgEditTec.setOnClickListener{
                    listener.editTec(model)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTechinqueBinding.inflate(inflater, parent, false)
        val viewHolder = TecViewHolder(binding)

        // Aplicar la animación cuando se crea el ViewHolder
        val scaleAnimation = AnimationUtils.loadAnimation(parent.context, R.anim.anim_one)
        viewHolder.itemView.startAnimation(scaleAnimation)

        return viewHolder
    }

    override fun onBindViewHolder(holder: TecViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
