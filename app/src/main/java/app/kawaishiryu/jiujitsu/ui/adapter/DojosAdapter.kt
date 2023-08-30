package app.kawaishiryu.jiujitsu.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.ItemListLocationBinding
import app.kawaishiryu.jiujitsu.util.OnItemClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class DojosAdapter(
    private val list: MutableList<DojosModel>,
    private val listener: OnItemClick?
) : RecyclerView.Adapter<DojosAdapter.DojosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DojosViewHolder {
        return DojosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(app.kawaishiryu.jiujitsu.R.layout.item_list_location, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DojosViewHolder, position: Int) {

        holder.render(list[position])

        with(holder.item_view) {
            setOnClickListener {
                listener?.setOnItemClickListener(list[position])
            }
        }

        holder.item_view.startAnimation(
            AnimationUtils.loadAnimation(
                holder.item_view.context,
                R.anim.anim_one
            )
        )

        holder.binding.ivDeleteDojo.setOnClickListener {
            listener?.onDeleteClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //View holder encargado de pintar el item del recyclerView
    class DojosViewHolder(val item_view: View) :
        RecyclerView.ViewHolder(item_view) {

        val binding = ItemListLocationBinding.bind(item_view)

        fun render(dojosModel: DojosModel) {

            if (dojosModel.nameDojo.isEmpty()) {
                binding.tvNameDojo.text = "No name"
            } else {
                binding.tvNameDojo.text = dojosModel.nameDojo
            }
            binding.tvSenseiName.text = dojosModel.nameSensei

            Glide.with(item_view)
                .load(dojosModel.dojoUrlImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivPhotoDojo)
            binding.ivDeleteDojo

        }
    }

}
