package kg.docplus.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.databinding.ActivityRegisterBinding
import kg.docplus.injection.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(RegisterViewModel::class.java)

        binding.viewModel = viewModel

    }

}
