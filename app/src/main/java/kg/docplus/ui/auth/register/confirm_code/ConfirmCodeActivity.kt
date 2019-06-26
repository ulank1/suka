package kg.docplus.ui.auth.register.confirm_code

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.databinding.ActivityConfirmCodeBinding
import kg.docplus.injection.ViewModelFactory

class ConfirmCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmCodeBinding
    private lateinit var viewModel: ConfirmCodeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DocPlusApp.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_code)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(ConfirmCodeViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.getSmsCode(this,intent.getStringExtra("phone"),intent.getStringExtra("password"),intent.getIntExtra("isRegister",0))
    }
}
