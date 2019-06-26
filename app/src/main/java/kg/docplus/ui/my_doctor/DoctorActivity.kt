package kg.docplus.ui.my_doctor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.databinding.ActivityFavouriteBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.utils.UserToken
import kotlinx.android.synthetic.main.activity_doctor.*

class DoctorActivity : AppCompatActivity() {

    private lateinit var viewModel: DoctorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        DocPlusApp.activity = this


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
