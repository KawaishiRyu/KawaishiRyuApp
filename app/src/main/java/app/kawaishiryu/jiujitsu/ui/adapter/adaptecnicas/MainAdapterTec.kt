package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.databinding.ItemMenuTechniqueBinding


class MainAdapterTec(private val collection: List<MainModelTec>) :
    RecyclerView.Adapter<MainAdapterTec.CollectionViewHolder>() {

    lateinit var context: Context

    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMenuTechniqueBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu_technique, parent, false)
        context = parent.context

        return CollectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {

        holder.binding.apply {

            val collection = collection[position]

            tvParentTitle.text = collection.title

            //val subItemAdapterTec = SubItemAdapterTec(collection.subItemModel)
            val subItemAdapterTec = collection.subItemModel?.let {
                SubItemAdapterTec(it) } //Veo q la lista sea distinta de nula

            //Añado un recycler View
            rvSubItem.adapter = subItemAdapterTec
            rvSubItem.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            tvParentTitle.setOnClickListener {
                if (collection.subItemModel != null){
                    rvSubItem.visibility = if (rvSubItem.isShown) View.GONE else View.VISIBLE
                }else{
                    imgMenu.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount() = collection.size
}