package kg.docplus.ui.auth.change_password.new_password

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.util.Log
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.DocPlusApp
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Product
import kg.docplus.network.PostApi
import kg.docplus.ui.auth.login.LoginActivity
import kg.docplus.ui.auth.register.RegisterActivity
import kg.docplus.ui.auth.register.confirm_code.ConfirmCodeActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class NewPasswordViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    var postList: ArrayList<Product> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    lateinit var phone:String

    private var subscription: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    private fun onRetrievePostListError() {

    }

    fun onClickNewPassword(password: EditText,repeatPassword: EditText) {
        var isValidate = true

        if (!password.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым.")) isValidate = false
        if (!password.validate({ s -> s == repeatPassword.text.toString() }, "Пароли не совпадают.")) isValidate = false

        Log.e("DDDD", isValidate.toString())

        if (isValidate) {

            subscription.add(
                postApi.newPassword(password.text.toString(),phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .doOnTerminate { hideProgress() }
                    .subscribe(
                        { result ->

                            if (result.isSuccessful) {

                                DocPlusApp.activity!!.toast("Пароль изменен")

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


}