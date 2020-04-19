package kg.docplus.ui.rating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import kg.docplus.R
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kg.docplus.injection.ViewModelFactory
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import kotlinx.android.synthetic.main.activity_rating.*


class RatingActivity : AppCompatActivity() {
    private lateinit var viewModel: PreviewModelView
    lateinit var adapter: RatingRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(PreviewModelView::class.java)
        setupRv()
        viewModel.getPreview(intent.getIntExtra("id",0))
        viewModel.preview.observe(this, Observer {
            if (it.isEmpty()){
                none.visible()
            }else {
                none.gone()
                adapter.swapData(it)
            }
        })
        back.setOnClickListener { finish() }
    }

    fun setupRv(){
        var manager = GridLayoutManager(this,1)
        rv.layoutManager = manager
        rv.setHasFixedSize(false)
        adapter = RatingRvAdapter(this)
        rv.adapter = adapter
    }
}
