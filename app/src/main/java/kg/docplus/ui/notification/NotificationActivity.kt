package kg.docplus.ui.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kotlinx.android.synthetic.main.activity_doctor.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var viewModel: NotificationViewModel
    lateinit var adapter: NotificationRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)



        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(NotificationViewModel::class.java)
        viewModel.myDoctor.observe(this, Observer { adapter.swapData(it) })

        back.setOnClickListener { finish() }
        setupRv()
        viewModel.getNotifications()


    }

    override fun onResume() {
        super.onResume()
        App.activity = this
    }

    fun setupRv(){
        var manager = GridLayoutManager(this,1)
       rv.layoutManager = manager
        rv.setHasFixedSize(false)
        adapter = NotificationRvAdapter(this)
        rv.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
