package kg.docplus.ui.auth.change_password.new_password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: kg.docplus.databinding.ActivityNewPasswordBinding
    private lateinit var viewModel: NewPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_password)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(NewPasswordViewModel::class.java)

        binding.viewModel = viewModel
        viewModel.phone = intent.getStringExtra("phone")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

    override fun onResume() {
        super.onResume()
        App.activity = this
    }

}
