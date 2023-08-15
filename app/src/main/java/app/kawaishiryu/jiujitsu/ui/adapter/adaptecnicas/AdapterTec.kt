package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.TecnicasFragmentDirections
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.databinding.ItemTechinqueBinding
import app.kawaishiryu.jiujitsu.util.OnItemClickTec

class AdapterTec(
    private val list: MutableList<MoviemientosModel>,
    private val nameFinal: String,
    private val listener: OnItemClickTec?
) : RecyclerView.Adapter<AdapterTec.TecViewHolder>() {

    class TecViewHolder(val itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding = ItemTechinqueBinding.bind(itemView)

        fun render(model: MoviemientosModel, nameFinal: String) {
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
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TecViewHolder {
        return TecViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_techinque, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TecViewHolder, position: Int) {
        val item = list[position]
        holder.render(item, nameFinal)

        holder.binding.imgDeleteTec.setOnClickListener {
            listener?.deleteTec(list[position])
        }

    }

    override fun getItemCount(): Int = list.size

}