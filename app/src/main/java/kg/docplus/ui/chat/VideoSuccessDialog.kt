package kg.docplus.ui.chat

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kg.docplus.R
import kg.docplus.utils.extension.setCircleImage

class VideoSuccessDialog(context: Context) : Dialog(context) {

    @SuppressLint("SetTextI18n")
    fun setUp(nameText: String, image_url: String, pirce:Int) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(R.layout.item_video_success)

        val name: TextView = findViewById(R.id.name)
        val pay:TextView = findViewById(R.id.price)
        val btnCancel: Button = findViewById(R.id.btn_cancel)
        val avatar: ImageView = findViewById(R.id.image_avatar)
        pay.text = "$pirce сом"
        setCircleImage(avatar,image_url,context)
        name.text = nameText

        btnCancel.setOnClickListener { cancel() }

        show()

    }
}