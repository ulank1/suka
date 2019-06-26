package kg.docplus.ui.auth.change_password

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.databinding.ActivityLoginBinding
import kg.docplus.injection.ViewModelFactory
class PhoneActivity : AppCompatActivity() {

    private lateinit var binding: kg.docplus.databinding.ActivityPhoneBinding
    private lateinit var viewModel: PhoneViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DocPlusApp.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(PhoneViewModel::class.java)

        binding.viewModel = viewModel

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
