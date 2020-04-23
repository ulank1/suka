package kg.docplus.ui.auth.login

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.quickblox.core.helper.StringifyArrayList
import com.quickblox.sample.core.utils.SharedPrefsHelper
import com.quickblox.users.model.QBUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.fcm.FCMTokenUtils
import kg.docplus.network.PostApi
import kg.docplus.qbwrtc.services.CallService
import kg.docplus.qbwrtc.utils.Consts
import kg.docplus.ui.auth.change_password.PhoneActivity
import kg.docplus.ui.auth.change_password.new_password.NewPasswordActivity
import kg.docplus.ui.auth.register.RegisterActivity
import kg.docplus.ui.main.MainActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()



    private var subscription: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    private fun onRetrievePostListError() {

    }

    fun onClickLogin(phone: EditText, password: EditText) {
        var isValidate = true

        if (!phone.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым.")) isValidate = false
        if (!password.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым.")) isValidate = false

        Log.e("DDDD",isValidate.toString())

        if (isValidate) {
            subscription.add(
                postApi.login(phone.text.toString(), password.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .doOnTerminate {  }
                    .subscribe(
                        { result ->
                            hideProgress()
                            if (result.isSuccessful) {

                                UserToken.saveToken(result.body()!!.token, App.activity!!)
                                Log.e("TOKsssaas",result.body()!!.toString())
                                saveUserData(result.body()!!.video_chat_credentials)
                                FCMTokenUtils.deleteToken(App.activity)

                                postFCMToken()
                            } else {
                                var error = result.errorBody()!!.string()
                                Log.e("Error",error)
                               // App.activity.startActivityForResult(Intent(App.activity,kg.docplus.qbwrtc.activities.LoginActivity::class.java).putExtra("login","dp${phone.text.toString().substring(1)}"),456)

                                if (error.contains("Невозможно войти с",true)){
                                    Log.e("TAF","DDD")
                                    App.activity!!.toast("Невозможно войти с предоставленными учетными данными")
                                }
                                if (error.contains("Unable to log-in with doctor account.")){
                                    App.activity!!.toast("Невозможно войти с аккаунта доктора в приложении для пациентов")
                                }
                            }

                        },
                        {
                            hideProgress()
                            Log.e("DDrergojpfD",it.toString())
                            onRetrievePostListError() }
                    )
            )
        }
    }


    private fun postFCMToken(){
        val registrationId = FCMTokenUtils.getTokenFromPrefs(App.activity!!)
        Log.e("REGISTER_ID",registrationId)

        subscription.add(
            postApi.postDeviceId(registrationId,"android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate {  }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("TOKENFCM",result.body().toString())
                            App.activity.startActivity(Intent(App.activity,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                            App.activity.finish()

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error",error)

                            if (error.contains("Невозможно войти с",true)){
                                Log.e("TAF","DDD")
                                App.activity.toast("Невозможно войти с предоставленными учетными данными")
                            }else if (error.contains("Unable to log-in with doctor account")){

                                App.activity.toast("Невозможно войти с аккаунтом доктора в приложение для пациентов")


                            }
                        }

                    },
                    {
                        hideProgress()
                        Log.e("DDrergojpfD",it.toString())
                        onRetrievePostListError() }
                )
        )

    }

    fun onClickForgotPassword(view: EditText) {

            val activity: LoginActivity = view.getParentActivity() as LoginActivity
            (activity).startActivityForResult(Intent(activity, PhoneActivity::class.java), 1)

    }

    fun onClickRegister(view: View) {

        App.activity!!.startActivity(Intent(App.activity,RegisterActivity::class.java))

    }
    private fun saveUserData(qbUser: QBUser) {
        var tags:StringifyArrayList<String> = StringifyArrayList()
        tags.add("Das")
        qbUser.tags = tags
        Log.e("QbUser", qbUser.toString())
        val sharedPrefsHelper = SharedPrefsHelper.getInstance()
        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.tags[0])
        sharedPrefsHelper.saveQbUser(qbUser)
        startLoginService(qbUser)
    }
    private fun startLoginService(qbUser: QBUser) {

        CallService.start(App.activity, qbUser)
    }

}