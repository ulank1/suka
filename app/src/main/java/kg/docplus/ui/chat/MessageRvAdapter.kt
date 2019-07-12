package kg.docplus.ui.chat

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import kg.docplus.R
import kotlin.collections.ArrayList
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kg.docplus.model.firebase.Message
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.extension.dateToChatFormat
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import kotlinx.android.synthetic.main.item_message.view.*
import java.util.*


class  MessageRvAdapter(val context: Context) : RecyclerView.Adapter<MessageRvAdapter.AdvertViewHolder>() {

    private var data: ArrayList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        return AdvertViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: ArrayList<Message>) {
        this.data = data
        notifyDataSetChanged()
    }


    inner class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Message) = with(itemView) {

            Log.e("ITEM", item.toString())

            val image: ImageView = itemView.findViewById(R.id.image)
            val lineOther: RelativeLayout = itemView.findViewById(R.id.line_other)
            val lineOwn: RelativeLayout = itemView.findViewById(R.id.line_own)



            if (item.user == 1) {
                lineOther.gone()
                lineOwn.visible()

                val imageOwn: ImageView = itemView.findViewById(R.id.image_own)
                val message: TextView = itemView.findViewById(R.id.message_own)
                val time: TextView = itemView.findViewById(R.id.time_own)
                var date = Date()
                date.time = item.time.toLong()

                time.text = dateToChatFormat(date)
                if (item.message.contains("http://doc.sunrisetest.site")) {
                    message.gone()
                    imageOwn.visible()
                    Glide.with(context).load(item.message)
                        .into(imageOwn)

                } else {
                    message.text = item.message
                    imageOwn.gone()
                    message.visible()
                }

                imageOwn.setOnClickListener { context.startActivity(Intent(context,ImageActivity::class.java).putExtra("image",item.message)) }

            } else {
                lineOwn.gone()
                lineOther.visible()
                val imageOther: ImageView = itemView.findViewById(R.id.image_other)
                val message: TextView = itemView.findViewById(R.id.message_other)
                val time: TextView = itemView.findViewById(R.id.time_other)

                var date = Date()
                date.time = item.time.toLong()

                time.text = dateToChatFormat(date)
                if (item.message.contains("http://doc.sunrisetest.site")) {
                    message.gone()
                    imageOther.visible()
                    Glide.with(context).load(item.message)
                        .into(imageOther)

                } else {
                    message.text = item.message
                    imageOther.gone()
                    message.visible()
                }

                Glide.with(context).load(Filter.chatAvatar)
                    .apply(
                        RequestOptions.bitmapTransform(
                            (CropCircleTransformation())
                        )
                    ).into(image)

                imageOther.setOnClickListener { context.startActivity(Intent(context,ImageActivity::class.java).putExtra("image",item.message)) }

            }


            itemView.setOnClickListener {

            }

        }
    }

}