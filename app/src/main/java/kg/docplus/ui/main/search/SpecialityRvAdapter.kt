package kg.docplus.ui.main.search

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kg.docplus.R
import kg.docplus.model.get.Specialties
import kotlin.collections.ArrayList

class SpecialityRvAdapter(val context: Context, val listener: FilterListener) : RecyclerView.Adapter<SpecialityRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<Specialties> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_speciality, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: ArrayList<Specialties>) {
        this.data = data
        notifyDataSetChanged()
    }



    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Specialties) = with(itemView) {

            val text:TextView = itemView.findViewById(R.id.text)
            text.text = item.title

            itemView.setOnClickListener {
                Log.e("Onclick","Onclick")
                listener.choose(text.text.toString(),item.id)
            }

        }
    }



}