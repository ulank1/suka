package kg.docplus.ui.doctor_deatail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kg.docplus.R
import kotlin.collections.ArrayList
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kg.docplus.App


class TimesRvAdapter(val context: Context,val listener: DetailListener) : RecyclerView.Adapter<TimesRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.times_item, parent, false)
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

            var time = item

            if (item.length>5){
                time = item.substring(0,5)
            }


            text.text = time

            itemView.setOnClickListener {
                listener.postAppointment(item)
            }

        }
    }



}