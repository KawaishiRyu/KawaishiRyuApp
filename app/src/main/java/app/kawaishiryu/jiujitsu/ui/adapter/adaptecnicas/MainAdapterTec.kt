package app.kawaishiryu.jiujitsu.ui.adapter.adaptecnicas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.databinding.ItemMenuTechniqueBinding
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.MenuJiuJitsuFragmentDirections
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.MenuJudoFragmentDirections


class MainAdapterTec(
    private val collection: List<MainModelTec>,
    private val isChecked: Boolean,
    private val menuTec: String

) : RecyclerView.Adapter<MainAdapterTec.CollectionViewHolder>() {
    lateinit var context: Context

    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemMenuTechniqueBinding.bind(itemView)

        fun render(collection: MainModelTec, bool: Boolean, menuTec: String, context: Context) {
            binding.apply {

                tvKanji.text = collection.kanji

                if (!bool) {
                    tvParentTitle.text = collection.title
                } else {
                    tvParentTitle.text = collection.translate
                }

                if (collection.subItemModel.isNullOrEmpty()) {
                    imgMenu.visibility = View.GONE

                } else {

                    imgMenu.visibility = View.VISIBLE

                    val subItemAdapterTec = SubItemAdapterTec(
                        collection.subItemModel,
                        bool,
                        collection.translate,
                        collection.kanji!!,
                        collection.description!!,
                        collection.title,
                        menuTec,
                    )

                    rvSubItem.adapter = subItemAdapterTec
                    rvSubItem.layoutManager =
                        LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                }

                //CardView Expandible
                itemView.setOnClickListener {
                    handleCardViewClick(collection, menuTec, context)
                }
            }
        }
        private fun handleCardViewClick(collection: MainModelTec, menuTec: String, context: Context) {
            when (menuTec) {
                "jiujitsu", "judo" -> {
                    if (collection.subItemModel != null) {
                        binding.rvSubItem.visibility =
                            if (binding.rvSubItem.isShown) View.GONE else View.VISIBLE
                    } else {
                        navigateToTecnicasFragment(collection, menuTec, context)
                    }
                }
                else -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun navigateToTecnicasFragment(collection: MainModelTec, menuTec: String, context: Context) {
            val navController = Navigation.findNavController(itemView)
            val action = when (menuTec) {
                "jiujitsu" -> MenuJiuJitsuFragmentDirections
                    .actionMenuJiuJitsuFragmentToTecnicasFragment(
                        collection.title,
                        null,
                        collection.translate,
                        collection.description!!,
                        collection.kanji?.toString() ?: ""
                    )
                "judo" -> MenuJudoFragmentDirections
                    .actionMenuJudoFragmentToTecnicasFragment(
                        collection.title,
                        null,
                        collection.translate,
                        collection.description!!,
                        collection.kanji?.toString() ?: ""
                    )
                else -> null
            }
            action?.let { navController.navigate(it) }
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

        holder.render(item, isChecked, menuTec, context)

        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.anim_one
            )
        )
    }

    override fun getItemCount() = collection.size
}
