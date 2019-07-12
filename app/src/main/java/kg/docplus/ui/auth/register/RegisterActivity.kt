package kg.docplus.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.databinding.ActivityRegisterBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.ui.auth.register.confirm_code.ConfirmCodeActivity
import kg.docplus.ui.main.MainActivity

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==456){
            val intent = Intent(App.activity, ConfirmCodeActivity::class.java)
            intent.putExtra("phone", binding.phone.text.toString())
            intent.putExtra("password", binding.password.text.toString())
            intent.putExtra("isRegister", 0)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        App.activity = this
    }

}
