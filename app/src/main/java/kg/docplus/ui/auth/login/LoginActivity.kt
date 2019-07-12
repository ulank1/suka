package kg.docplus.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.databinding.ActivityLoginBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(LoginViewModel::class.java)

        binding.viewModel = viewModel

    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
        if (requestCode ==456){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }*/

    override fun onResume() {
        super.onResume()
        App.activity = this
    }

}
