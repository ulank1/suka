package kg.docplus.ui.main.search

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
import kg.docplus.ui.doctor_deatail.DoctorDetailActivity
import kotlin.collections.ArrayList

class DoctorRvAdapter(val context: Context) : RecyclerView.Adapter<DoctorRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<DoctorGet> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_doctor, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<DoctorGet>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        data.clear()
    }

    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: DoctorGet) = with(itemView) {

            val name:TextView = itemView.findViewById(R.id.name)
            val specialities:TextView = itemView.findViewById(R.id.specialities)
            val minPrice:TextView = itemView.findViewById(R.id.min_price)
            val patients:TextView = itemView.findViewById(R.id.patients_count)
            val rating:TextView = itemView.findViewById(R.id.rating)
            val avatar:ImageView  = itemView.findViewById(R.id.avatar)

            name.text = "${item.doctor_detail.first_name} ${item.doctor_detail.mid_name}"

            for (speciality in item.doctor_detail.specialties){
                specialities.append(speciality.title)
            }

            minPrice.text = "${item.min_price} сом"
            if (item.doctor_detail.avatar!= null)
            setImage(item.doctor_detail.avatar,avatar)

            itemView.setOnClickListener {
                Log.e("ID_DOCTOR",item.id.toString())
                App.activity!!.startActivity(Intent(App.activity, DoctorDetailActivity::class.java))
            }


        }
    }

    fun setImage(image:String,avatar:ImageView){
        Glide.with(App.activity!!).load(image).into(avatar)

    }


}