package kg.docplus.ui.doctor_deatail

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kg.docplus.R
import kotlin.collections.ArrayList
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import kg.docplus.App
import kg.docplus.model.get.UrlImage
import kg.docplus.ui.chat.ImageActivity
import kg.docplus.utils.extension.setRoundedImage


class ImageRvAdapter(val context: Context) : RecyclerView.Adapter<ImageRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<UrlImage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.image_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: ArrayList<UrlImage>) {
        this.data = data
        notifyDataSetChanged()
    }



    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: UrlImage) = with(itemView) {

            val image:ImageView = itemView.findViewById(R.id.image)
//            Glide.with(App.activity!!).load(item).transform(CenterCrop(),RoundedCorners(16)).into(image)


            setRoundedImage(image,item.file,context)

            itemView.setOnClickListener {
                context.startActivity(
                        Intent(
                                context,
                                ImageActivity::class.java
                        ).putExtra("image", item.file)
                )
            }

        }
    }



}