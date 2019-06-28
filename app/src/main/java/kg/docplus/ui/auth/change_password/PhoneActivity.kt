package kg.docplus.ui.auth.change_password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
class PhoneActivity : AppCompatActivity() {

    private lateinit var binding: kg.docplus.databinding.ActivityPhoneBinding
    private lateinit var viewModel: PhoneViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(PhoneViewModel::class.java)

        binding.viewModel = viewModel

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
