package kg.docplus.ui.chat

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kg.docplus.R
import kotlinx.android.synthetic.main.activity_image.*



class ImageActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        Glide.with(this).load(intent.getStringExtra("image")).into(myZoomageView)

        back.setOnClickListener { finish() }
    }
}
