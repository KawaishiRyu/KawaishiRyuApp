package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.MenuTecFragmentDirections
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.SubItemModelTec
import app.kawaishiryu.jiujitsu.databinding.SubListItemBinding


class SubItemAdapterTec(
    private val subItemModel: List<SubItemModelTec>,
    private val bool: Boolean,
    private val part1Translate: String
) : RecyclerView.Adapter<SubItemAdapterTec.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = SubListItemBinding.bind(itemView)

        fun render(subItem: SubItemModelTec, bool: Boolean, part1Translate: String) {
            binding.apply {

                if (!bool) {
                    tvSubItemTitle.text = subItem.subItemTitle
                } else {
                    tvSubItemTitle.text = subItem.translateTitle.toString()
                }

                val word = subItem.subItemTitle

                itemView.setOnClickListener {
                    val subItemTitle = binding.tvSubItemTitle.text.toString()
                    val context = itemView.context

                    Toast.makeText(context, "Clicked: $subItemTitle", Toast.LENGTH_SHORT).show()

                    val navController = Navigation.findNavController(itemView)

                    val action = MenuTecFragmentDirections.actionMenuTecFragmentToTecnicasFragment(
                        null,
                        word,
                        "$part1Translate: ${subItem.translateTitle}"
                    )
                    navController.navigate(action)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sub_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subItem = subItemModel[position]

        holder.render(subItem, bool, part1Translate)

    }

    override fun getItemCount() = subItemModel.size


}