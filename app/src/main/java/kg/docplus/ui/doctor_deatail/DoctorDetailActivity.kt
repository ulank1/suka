package kg.docplus.ui.doctor_deatail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.databinding.ActivityLoginBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.ui.login.LoginViewModel

class DoctorDetailActivity : AppCompatActivity() {

    private lateinit var binding: kg.docplus.databinding.ActivityDoctorDetailBinding
    private lateinit var viewModel: DoctorDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DocPlusApp.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(DoctorDetailViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.active.value = false

        viewModel.getDoctorFull(intent.getIntExtra("id",7))
        viewModel.doctor.observe(this, Observer { binding.doctor = it })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
