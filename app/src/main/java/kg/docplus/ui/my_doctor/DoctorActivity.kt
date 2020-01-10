package kg.docplus.ui.my_doctor

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

class DoctorActivity : AppCompatActivity(), MydoctorListener {
    override fun sendPush(patient_id: String?) {
        viewModel.postNotification(patient_id)
    }

    private lateinit var viewModel: DoctorViewModel
    lateinit var adapter: MyDoctorRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(DoctorViewModel::class.java)
        viewModel.myDoctor.observe(this, Observer { adapter.swapData(it) })

        back.setOnClickListener { finish() }
        setupRv()
        viewModel.getDoctorFavourite()


    }

    override fun onResume() {
        super.onResume()
        App.activity = this
    }

    fun setupRv(){
        var manager = GridLayoutManager(this,1)
       rv.layoutManager = manager
        rv.setHasFixedSize(false)
        adapter = MyDoctorRvAdapter(this,this)
        rv.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
