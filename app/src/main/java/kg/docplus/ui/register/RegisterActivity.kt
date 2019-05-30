package kg.docplus.ui.register

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.databinding.ActivityRegisterBinding
import kg.docplus.injection.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DocPlusApp.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(RegisterViewModel::class.java)

        binding.viewModel = viewModel

    }

}
