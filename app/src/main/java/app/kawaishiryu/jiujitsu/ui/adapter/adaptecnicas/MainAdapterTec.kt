package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.databinding.ItemMenuTechniqueBinding
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.MenuTecFragmentDirections
import app.kawaishiryu.jiujitsu.util.OnItemClick


class MainAdapterTec(
    private val collection: List<MainModelTec>,
    private val listener :OnItemClick?,
    private val isChecked : Boolean
) : RecyclerView.Adapter<MainAdapterTec.CollectionViewHolder>() {

    lateinit var context: Context


    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemMenuTechniqueBinding.bind(itemView)

        fun render(collection: MainModelTec, bool :Boolean) {
            binding.apply {

                if (!bool){
                    tvParentTitle.text = collection.title
                }else{
                    tvParentTitle.text = collection.translate
                }

                if (collection.subItemModel.isNullOrEmpty()) {
                    imgMenu.visibility = View.GONE
                } else {

                    imgMenu.visibility = View.VISIBLE

                    val subItemAdapterTec = SubItemAdapterTec(collection.subItemModel, bool, collection.translate)

                    rvSubItem.adapter = subItemAdapterTec
                    rvSubItem.layoutManager =
                        LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                }

                //CardView Expandible
                itemView.setOnClickListener {

                    if (collection.subItemModel != null) {
                        rvSubItem.visibility = if (rvSubItem.isShown) View.GONE else View.VISIBLE
                    } else {
                        val navController = Navigation.findNavController(itemView)

                        val action = MenuTecFragmentDirections.actionMenuTecFragmentToTecnicasFragment(collection.title, null, collection.translate)
                        navController.navigate(action)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu_technique, parent, false)
        context = parent.context

        return CollectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val item = collection[position]

        holder.render(item, isChecked)
    }
    override fun getItemCount() = collection.size


}
