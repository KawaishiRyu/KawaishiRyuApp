package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.SubItemModelTec
import app.kawaishiryu.jiujitsu.databinding.SubListItemBinding
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.MenuJiuJitsuFragmentDirections
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.MenuJudoFragmentDirections


class SubItemAdapterTec(
    private val subItemModel: List<SubItemModelTec>,
    private val bool: Boolean,
    private val part1Translate: String,
    private val kanji: String,
    private val description: Int,
    private val tecnica: String,
    private val optionMenu: String,

) : RecyclerView.Adapter<SubItemAdapterTec.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = SubListItemBinding.bind(itemView)

        fun render(
            subItem: SubItemModelTec,
            bool: Boolean,
            part1Translate: String,
            kanji: String,
            description: Int,
            tecnica: String,
            optionMenu: String
        ) {

            binding.apply {

                if (!bool) {
                    tvSubItemTitle.text = subItem.subItemTitle
                } else {
                    tvSubItemTitle.text = subItem.translateTitle.toString()
                }

                val nameComplte = "$tecnica: ${subItem.subItemTitle}"

                itemView.setOnClickListener {
                    binding.tvSubItemTitle.text.toString()

                    val navController = Navigation.findNavController(itemView)

                    if (optionMenu == "jiujitsu") {
                        val action =
                            MenuJiuJitsuFragmentDirections.actionMenuJiuJitsuFragmentToTecnicasFragment(
                                null,
                                nameComplte,
                                "$part1Translate: ${subItem.translateTitle}",
                                description,
                                kanji,
                            )
                        navController.navigate(action)
                    }else{
                        val action =
                            MenuJudoFragmentDirections.actionMenuJudoFragmentToTecnicasFragment(
                                null,
                                nameComplte,
                                "$part1Translate: ${subItem.translateTitle}",
                                description,
                                kanji,
                            )
                        navController.navigate(action)
                    }
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

        holder.render(subItem, bool, part1Translate, kanji, description, tecnica, optionMenu)

    }

    override fun getItemCount() = subItemModel.size
}