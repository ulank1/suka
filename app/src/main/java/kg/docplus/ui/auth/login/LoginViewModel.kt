package kg.docplus.ui.auth.login

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.network.PostApi
import kg.docplus.ui.auth.change_password.new_password.NewPasswordActivity
import kg.docplus.ui.auth.register.RegisterActivity
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
                    .doOnTerminate { hideProgress() }
                    .subscribe(
                        { result ->

                            if (result.isSuccessful) {

                                //UserToken.saveToken(result.body()!!.token, App.context!!)
                                Log.e("TOK",result.body()!!.toString())

                            } else {
                                var error = result.errorBody()!!.string()
                                Log.e("Error",error)

                                if (error.contains("Невозможно войти с",true)){
                                    Log.e("TAF","DDD")
                                    App.activity!!.toast("Невозможно войти с предоставленными учетными данными")
                                }
                            }

                        },
                        {
                            Log.e("DDD",it.toString())
                            onRetrievePostListError() }
                    )
            )
        }
    }

    fun onClickForgotPassword(view: View) {

        val activity: LoginActivity = view.getParentActivity() as LoginActivity
        (activity).startActivityForResult(Intent(activity, NewPasswordActivity::class.java), 1)

    }

    fun onClickRegister(view: View) {

        App.activity!!.startActivity(Intent(App.activity,RegisterActivity::class.java))

    }


}