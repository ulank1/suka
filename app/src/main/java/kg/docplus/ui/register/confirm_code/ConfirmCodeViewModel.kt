package kg.docplus.ui.register.confirm_code

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.DocPlusApp
import kg.docplus.DocPlusApp.Companion.activity
import kg.docplus.DocPlusApp.Companion.context
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Product
import kg.docplus.network.PostApi
import kg.docplus.post.PostListActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfirmCodeViewModel : BaseViewModel() {
    @Inject
    lateinit var postApi: PostApi
    var postList: ArrayList<Product> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    lateinit var phone: String
    lateinit var password: String
    var isRegister: Boolean = false
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
                    onVerificationComplete()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

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

    fun getSmsCode(activity: Activity, phone: String, password: String, isRegister: Boolean) {
        context = activity
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
          if (isRegister) {
              register()
          } else {
              // context.startActivity(Intent(context, NewPasswordActivity::class.java).putExtra("phone", phone))
          }
    }

    fun register() {

        subscription.add(
            postApi.register(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe(
                    { result ->

                        if (result.isSuccessful) {

                            UserToken.saveToken(result.body()!!.token, DocPlusApp.activity!!)
//                            activity?.startActivity(Intent(this,EditProfileActivity::class.java))

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error", error)

                            if (error.contains("Невозможно войти с", true)) {
                                Log.e("TAF", "DDD")
                                DocPlusApp.activity!!.toast("Невозможно войти с предоставленными учетными данными")
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


}