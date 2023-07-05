package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.SubItemModelTec
import app.kawaishiryu.jiujitsu.databinding.SubListItemBinding


class SubItemAdapterTec(private val subItemModel:List<SubItemModelTec>):
    RecyclerView.Adapter<SubItemAdapterTec.ViewHolder>()
{
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = SubListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvSubItemTitle.text = subItemModel[position].subItemTitle
        }
    }

    override fun getItemCount() = subItemModel.size


}