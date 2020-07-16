package kg.docplus.ui.notification

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kg.docplus.R
import kotlinx.android.synthetic.main.activity_paybox.*
import money.paybox.payboxsdk.PayboxSdk

class PayboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paybox)

        var sdk = PayboxSdk.initialize(525789, "DQPCXraB5KhM5o0Q")
        sdk.setPaymentView(paymentView)
        var params = HashMap<String,String>()
        sdk.config().testMode(true)
//        sdk.config().setPaymentLifetime(1000)
        sdk.config().setCurrencyCode("KGS")
        var price = intent.getIntExtra("price",1).toFloat()
        sdk.createPayment(price, "description", "orderId", 1234, params) {
            payment, error ->
            run {
                Log.e("PAY", error.toString() + " / " + payment.toString())
                if (payment!=null&&error==null) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }else if (payment==null&&error!=null){
                    var intent = Intent()
                    intent.putExtra("error",error.description)
                    setResult(Activity.RESULT_CANCELED,intent)
                    finish()
                }else{
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}
