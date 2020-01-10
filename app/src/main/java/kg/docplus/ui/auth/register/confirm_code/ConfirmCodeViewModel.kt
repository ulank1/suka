package kg.docplus.ui.auth.register.confirm_code

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.App.activity
import kg.docplus.base.BaseViewModel
import kg.docplus.fcm.FCMTokenUtils
import kg.docplus.network.PostApi
import kg.docplus.ui.auth.change_password.new_password.NewPasswordActivity
import kg.docplus.ui.auth.register.SharedVideo
import kg.docplus.ui.main.MainActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfirmCodeViewModel : BaseViewModel() {
    @Inject
    lateinit var postApi: PostApi
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    lateinit var phone: String
    lateinit var password: String
    var isRegister: Int = 0
    private val TAG = this::class.java.simpleName
    private var storedVerificationId: String? = ""
    private var auth = FirebaseAuth.getInstance()
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private var subscription: CompositeDisposable = CompositeDisposable()

    private var mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            Log.e(TAG, "onVerificationCompleted:$credential")

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            Log.e(TAG, "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                activity?.toast("Формат телефонного номера введен неправильно.")
            } else if (e is FirebaseTooManyRequestsException) {

            }

        }

        override fun onCodeSent(
            verificationId: String?,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.e(TAG, "onCodeSent")
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {


        val a = auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "signed in successfully")
                    Log.e("ISreg",isRegister.toString())
                    onVerificationComplete()
                } else {
                        Log.e("ERROR",task.exception.toString())
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e("LOH","LOH")
                    }
                }
            }
    }

    private fun startPhoneNumberVerification(activity: Activity, phoneNumber: String) {


        Log.e(TAG, phoneNumber)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity,
            mCallBacks
        )

    }

    fun getSmsCode(activity: Activity, phone: String, password: String, isRegister: Int) {

        this.isRegister = isRegister
        this.password = password
        this.phone = phone
        startPhoneNumberVerification(activity, phone)
    }

    private fun verifyPhoneNumberWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            activity, // Activity (for callback binding)
            mCallBacks, // OnVerificationStateChangedCallbacks
            resendToken
        ) // ForceResendingToken from callbacks
    }

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    private fun onRetrievePostListError() {

    }

    fun sendCode(code: String) {
        if (code.isNotEmpty()) {
            verifyPhoneNumberWithCode(code)
        } else {
            activity?.toast("Наберите Код")
        }
    }

    private fun onVerificationComplete() {
          if (isRegister==0) {
              register()
          } else if (isRegister==1){
                App.activity?.startActivity(Intent(App.activity!!,NewPasswordActivity::class.java).putExtra("phone",phone))
          }else{

          }
    }

    fun register() {
        var qbUser = SharedVideo.qbUser
        var json = JsonObject()
        json.addProperty("id",qbUser.id)
        json.addProperty("full_name",qbUser.fullName)
        json.addProperty("login",qbUser.login)
        json.addProperty("password",qbUser.password)
        var tags = JsonArray()
        tags.add(qbUser.tags[0])
        json.add("tags",tags)

        Log.e("QSSSQDFF",json.toString())
        subscription.add(
            postApi.register(phone, password,json.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe(
                    { result ->

                        if (result.isSuccessful) {
                            Log.e("jhdjsdsj",result.body().toString())

                            UserToken.saveToken(result.body()!!.token, App.activity!!)
                            FCMTokenUtils.deleteToken(App.activity)

                            postFCMToken()

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error", error)

                            if (error.contains("Невозможно войти с", true)) {
                                Log.e("TAF", "DDD")
                                App.activity!!.toast("Невозможно войти с предоставленными учетными данными")
                            }
                        }

                    },
                    {
                        Log.e("DDD", it.toString())
                        onRetrievePostListError()
                    }
                )
        )
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
                            App.activity.startActivity(Intent(App.activity,MainActivity::class.java))

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error",error)

                            if (error.contains("Невозможно войти с",true)){
                                Log.e("TAF","DDD")
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