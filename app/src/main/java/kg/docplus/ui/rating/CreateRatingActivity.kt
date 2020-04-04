package kg.docplus.ui.rating

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kg.docplus.R
import kotlinx.android.synthetic.main.activity_create_rating.*
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.ViewModelProviders
import kg.docplus.injection.ViewModelFactory
import kg.docplus.ui.main.search.FilterViewModel


class CreateRatingActivity : AppCompatActivity() {
    private lateinit var viewModel: PreviewModelView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rating)
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(PreviewModelView::class.java)


        val stars = rating.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
        stars.getDrawable(0).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP)
        stars.getDrawable(1).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP)
        btn_send.setOnClickListener {

            Log.e("RAting",rating.rating.toString())

        }

    }
}
