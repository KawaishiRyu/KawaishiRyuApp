package app.kawaishiryu.jiujitsu.domain.home.adapeter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.core.BaseViewHolder
import app.kawaishiryu.jiujitsu.data.model.Dojos
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterDojoBinding
import app.kawaishiryu.jiujitsu.databinding.ItemListLocationBinding
import com.squareup.picasso.Picasso

class LocationDojoAdapter(private val dojoList: List<Dojos>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            ItemListLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DojoScreenViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is DojoScreenViewHolder -> holder.bind(dojoList[position])
        }
    }

    //Retorna la cantidad de elementos que tiene la lista
    override fun getItemCount(): Int {
        return dojoList.size
    }

    private inner class DojoScreenViewHolder(
        val binding: ItemListLocationBinding,
        val context: Context //Si se utiliza glide
    ): BaseViewHolder<Dojos>(binding.root) {

        override fun bind(item: Dojos) {
            Picasso.get()
                .load(item.urlImageDojo)
                .into(binding.ivPhotoDojo)

            binding.tvNameDojo.text = item.nameDojo
            binding.tvLocation.text = item.ubication
        }
    }
}