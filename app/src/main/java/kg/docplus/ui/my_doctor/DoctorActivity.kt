package kg.docplus.ui.my_doctor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kotlinx.android.synthetic.main.activity_doctor.*

class DoctorActivity : AppCompatActivity() {

    private lateinit var viewModel: DoctorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        App.activity = this


        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(DoctorViewModel::class.java)



        setupRv()
        viewModel.getDoctorFavourite()


    }

    fun setupRv(){
        var manager = GridLayoutManager(this,2)
       rv.layoutManager = manager
        rv.setHasFixedSize(false)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
