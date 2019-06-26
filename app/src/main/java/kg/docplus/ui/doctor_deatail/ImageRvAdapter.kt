package kg.docplus.ui.doctor_deatail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kg.docplus.DocPlusApp
import kg.docplus.R
import kotlin.collections.ArrayList
import com.bumptech.glide.load.resource.bitmap.CenterCrop


class ImageRvAdapter(val context: Context) : RecyclerView.Adapter<ImageRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.image_item, parent, false)
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

            val image:ImageView = itemView.findViewById(R.id.image)
            Glide.with(DocPlusApp.activity!!).load(item).transform(CenterCrop(),RoundedCorners(16)).into(image)

            itemView.setOnClickListener {

            }

        }
    }



}