package kg.docplus.ui.my_doctor

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import kg.docplus.App
import kg.docplus.R
import kg.docplus.model.get.DoctorGet
import kg.docplus.model.get.my_doctor.MyDoctor
import kg.docplus.ui.chat.ChatActivity
import kg.docplus.ui.chat.ImageActivity
import kg.docplus.ui.doctor_deatail.DoctorDetailActivity
import kg.docplus.utils.extension.*
import kotlin.collections.ArrayList

class MyDoctorRvAdapter(val context: Context) : RecyclerView.Adapter<MyDoctorRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<MyDoctor> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_doctor, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<MyDoctor>) {
        this.data.addAll(data)
        notifyDataSetChanged()
        Log.e("DATARR", data.toString())
    }

    fun clearData() {
        data.clear()
    }

    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MyDoctor) = with(itemView) {

            val name: TextView = itemView.findViewById(R.id.name)
            val date: TextView = itemView.findViewById(R.id.date)
            val titleService: TextView = itemView.findViewById(R.id.title_service)
            val specialities: TextView = itemView.findViewById(R.id.specialities)
            val rating: TextView = itemView.findViewById(R.id.rating)
            val avatar: ImageView = itemView.findViewById(R.id.avatar)
            val action: ImageView = itemView.findViewById(R.id.btn_action)
            val serviceIcon: ImageView = itemView.findViewById(R.id.icon_of_service)

            name.text = "${item.doctor_detail.first_name} ${item.doctor_detail.mid_name}"

            setService(serviceIcon, titleService, item.service)

            specialities.text = ""

            for ((i, text) in item.doctor_detail.specialties.withIndex()) {
                if (i == item.doctor_detail.specialties.size - 1) {
                    specialities.append(text.title)
                } else {
                    specialities.append("${text.title}, ")
                }
            }

            date.text = getDate(item.date,item.exact_time)
            avatar.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        ImageActivity::class.java
                    ).putExtra("image", item.doctor_detail.avatar)
                )
            }

            if (item.doctor_detail.avatar != null)
                setImage(item.doctor_detail.avatar, avatar)

            if (item.service == 1) {
                action.visible()
                action.setOnClickListener {
                    var intent = Intent(context,ChatActivity::class.java)
                    intent.putExtra("doc_id",item.doctor_detail.id.toString())
                    intent.putExtra("patient_id",item.id.toString())
                    intent.putExtra("avatar",item.doctor_detail.avatar)
                    intent.putExtra("speciality",specialities.text.toString())
                    intent.putExtra("time",item.date+" "+item.exact_time)
                    intent.putExtra("name",name.text.toString())
                    context.startActivity(intent) }
            } else {
                action.gone()
            }

            itemView.setOnClickListener {
                //                App.activity!!.startActivity(Intent(App.activity, DoctorDetailActivity::class.java).putExtra("id",item.id))
            }


        }
    }

    fun setImage(image: String, avatar: ImageView) {
        setRoundedImage(avatar,image,context)

    }

    fun getDate(date: String, time: String): String {
        return time + " | " + getDayOfWeekName(date) + " " + date
    }

    fun setService(image: ImageView, text: TextView, service: Int) {

        if (service == 0) {
            image.setImageResource(R.drawable.appointment_main)
            text.text = getServiceName(service)
        }
        if (service == 1) {
            image.setImageResource(R.drawable.chat_main)
            text.text = getServiceName(service)
        }
        if (service == 2) {
            image.setImageResource(R.drawable.video_main)
            text.text = getServiceName(service)
        }
        if (service == 3) {
            image.setImageResource(R.drawable.call_home_main)
            text.text = getServiceName(service)
        }

    }


}