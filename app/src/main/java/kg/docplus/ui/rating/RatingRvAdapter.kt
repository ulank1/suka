package kg.docplus.ui.rating

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kg.docplus.R
import kg.docplus.model.get.Notification
import kg.docplus.model.get.Preview
import kg.docplus.ui.my_doctor.DoctorActivity
import kg.docplus.utils.extension.formatDatePreview
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import kotlinx.android.synthetic.main.item_rating.view.*
import kotlin.collections.ArrayList


class RatingRvAdapter(val context: Context) : RecyclerView.Adapter<RatingRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<Preview> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rating, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: ArrayList<Preview>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        data.clear()
    }

    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Preview) = with(itemView) {

            itemView.desc.text = item.comment
            itemView.name.text = item.patient.patient_detail.first_name
            itemView.rate.text = item.rate.toString()
            itemView.date.text = formatDatePreview(item.created_at)
            itemView.setOnClickListener { }

        }
    }
}