package kg.docplus.ui.main.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kotlinx.android.synthetic.main.activity_send_fail.*

class SendFailActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    var type = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_fail)
        App.activity = this
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(SettingsViewModel::class.java)

        type = intent.getIntExtra("type",1)

        back.setOnClickListener {

            finish()

        }

        if (type==1){
            title1.text = "Сообщить об ошибке"
            message.hint = "Сообщение об ошибке"
        }else{
            title1.text = "Служба поддержки"
            message.hint = "Сообщение"
        }

        btn_ok.setOnClickListener {

            if (message.text.toString().isNotEmpty()) {

                if (type == 1) {
                    viewModel.postReport(message.text.toString())
                }else{
                    viewModel.postSupport(message.text.toString())
                }
            }else{
                message.error = "Напишите сообщение"
            }

        }
    }
}
