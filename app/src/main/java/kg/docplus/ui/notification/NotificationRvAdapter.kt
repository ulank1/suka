package kg.docplus.ui.notification

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kg.docplus.R
import kg.docplus.model.get.Notification
import kg.docplus.ui.my_doctor.DoctorActivity
import kotlin.collections.ArrayList


class NotificationRvAdapter(val context: Context) : RecyclerView.Adapter<NotificationRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: ArrayList<Notification>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        data.clear()
    }

    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Notification) = with(itemView) {

            val name:TextView = itemView.findViewById(R.id.title)
            val desc:TextView = itemView.findViewById(R.id.desc)

            name.text = item.title
            desc.text = item.body

            itemView.setOnClickListener { context.startActivity(Intent(context,DoctorActivity::class.java)) }

        }
    }
}