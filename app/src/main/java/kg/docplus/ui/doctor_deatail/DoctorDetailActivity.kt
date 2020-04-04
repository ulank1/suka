package kg.docplus.ui.doctor_deatail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.setRoundedImage

class DoctorDetailActivity : AppCompatActivity() {

    private lateinit var binding: kg.docplus.databinding.ActivityDoctorDetailBinding
    private lateinit var viewModel: DoctorDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(DoctorDetailViewModel::class.java)

        binding.viewModel = viewModel
        viewModel.getCities()

        viewModel.active.value = false
        setupRv()
        viewModel.getDoctorFull(intent.getIntExtra("id", 7))
        viewModel.doctor.observe(this, Observer {
            binding.doctor = it
            setRoundedImage(binding.avatar,it.doctor_detail.avatar.file,this@DoctorDetailActivity)

        })
        Log.e("TOKEN", UserToken.getToken(this))

    }

    private fun setupRv() {
        var manager = GridLayoutManager(this, 2)
        binding.rv.layoutManager = manager
        binding.rv.setHasFixedSize(false)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode", requestCode.toString())
    }

    override fun onResume() {
        super.onResume()
        App.activity = this
    }

}
