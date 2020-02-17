package kg.docplus.ui.chat

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

class VideoCancelDialog(context: Context) : Dialog(context) {

    fun setUp(nameText: String, image_url: String) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(R.layout.item_video_cancel)

        var name: TextView = findViewById(R.id.name)
        var btnCancel: Button = findViewById(R.id.btn_cancel)
        var avatar: ImageView = findViewById(R.id.image_avatar)

        setCircleImage(avatar,image_url,context)
        name.text = nameText

        btnCancel.setOnClickListener { cancel() }

        show()

    }
}