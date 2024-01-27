package app.kawaishiryu.jiujitsu.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class GradoDropdownAdapter(
    context: Context,
    resource: Int,
    items: List<String>
) : ArrayAdapter<String>(context, resource, items) {

    private val originalItems: List<String> = ArrayList(items)

    override fun getFilter(): Filter {
        return gradoFilter
    }

    private val gradoFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val filteredList: MutableList<String> = ArrayList()

            // No hay restricción, muestra la lista original
            filteredList.addAll(originalItems)

            results.values = filteredList
            results.count = filteredList.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            clear()
            addAll(results.values as List<String>)
            notifyDataSetChanged()
        }
    }
}