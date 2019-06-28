package kg.docplus.ui.main.search

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kg.docplus.R
import kotlin.collections.ArrayList

class SpecialityRvAdapter(val context: Context, val listener: FilterListener) : RecyclerView.Adapter<SpecialityRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_speciality, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: ArrayList<String>) {
        this.data = data
        notifyDataSetChanged()
    }



    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) = with(itemView) {

            val text:TextView = itemView.findViewById(R.id.text)
            text.text = item

            itemView.setOnClickListener {
                listener.choose(text.text.toString())
            }

        }
    }



}