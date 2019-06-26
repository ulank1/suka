package kg.docplus.ui.auth.change_password.new_password

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
class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: kg.docplus.databinding.ActivityNewPasswordBinding
    private lateinit var viewModel: NewPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DocPlusApp.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_password)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(NewPasswordViewModel::class.java)

        binding.viewModel = viewModel
        viewModel.phone = intent.getStringExtra("phone")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
