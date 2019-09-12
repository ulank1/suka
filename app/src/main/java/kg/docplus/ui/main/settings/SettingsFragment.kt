package kg.docplus.ui.main.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.docplus.App
import kotlinx.android.synthetic.main.fragment_settings.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.messages.services.SubscribeService
import com.quickblox.sample.core.utils.SharedPrefsHelper
import com.quickblox.users.QBUsers
import kg.docplus.R
import kg.docplus.qbwrtc.activities.LoginActivity
import kg.docplus.qbwrtc.services.CallService
import kg.docplus.qbwrtc.utils.UsersUtils
import kg.docplus.utils.UserToken

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            App.activity.onBackPressed()
        }

        rate_app.setOnClickListener {
            val appPackageName = App.activity.packageName // getPackageName() from Context or Activity object
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        recommend.setOnClickListener {

            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "Here is the share content body"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))

        }

        sign_out.setOnClickListener {

            UserToken.clearToken(App.activity)
            logOut()

        }

        terms.setOnClickListener{
            startActivity(Intent(context!!,TesrmsActivity::class.java))
        }

        fails. setOnClickListener {
            startActivity(Intent(context!!,TesrmsActivity::class.java).putExtra("type",1))

        }
        support.setOnClickListener {
            startActivity(Intent(context!!,TesrmsActivity::class.java).putExtra("type",2))

        }

    }

    private fun logOut() {
        unsubscribeFromPushes()
        startLogoutCommand()
        removeAllUserData()
        startLoginActivity()
    }

    private fun startLogoutCommand() {
        CallService.logout(App.activity)
    }

    private fun unsubscribeFromPushes() {
        SubscribeService.unSubscribeFromPushes(App.activity)
    }

    private fun removeAllUserData() {

        var sharedPrefsHelper = SharedPrefsHelper.getInstance()
        QBUsers.deleteUser(sharedPrefsHelper.qbUser.id)
        UsersUtils.removeUserData(App.activity)
    }

    private fun startLoginActivity() {
        App.activity.startActivity(Intent(App.activity, kg.docplus.ui.auth.login.LoginActivity::class.java))
        App.activity.finish()
    }

}
