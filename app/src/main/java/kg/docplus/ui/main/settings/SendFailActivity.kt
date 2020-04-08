package kg.docplus.ui.main.settings

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kg.docplus.utils.extension.getServiceName
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

        viewModel.sendFail.observe(this, Observer {

            if (it){
                showAlertOK()
            }else{
                showAlertFail()
            }

        })

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

    private fun showAlertOK() {

        var dialogResult = Dialog(this)
        dialogResult.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogResult.setCancelable(false)
        dialogResult.setContentView(R.layout.customviews)


        val btnOk = dialogResult.findViewById(R.id.button_ok) as Button

        btnOk.setOnClickListener {
            finish()
            dialogResult.cancel() }
        dialogResult.show()

    }
    private fun showAlertFail() {

        var dialogResult = Dialog(this)
        dialogResult.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogResult.setCancelable(false)
        dialogResult.setContentView(R.layout.customview)


        val btnOk = dialogResult.findViewById(R.id.buttonOk) as Button

        btnOk.setOnClickListener {
            finish()
            dialogResult.cancel() }
        dialogResult.show()

    }
}
