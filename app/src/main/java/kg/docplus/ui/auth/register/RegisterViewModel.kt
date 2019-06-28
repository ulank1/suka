package kg.docplus.ui.auth.register

import android.content.Intent
import android.util.Log
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.network.PostApi
import kg.docplus.ui.auth.login.LoginActivity
import kg.docplus.ui.auth.register.confirm_code.ConfirmCodeActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class RegisterViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi


    private var subscription: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    private fun onRetrievePostListError() {

    }

    fun onClickRegister(phone: EditText, password: EditText, repeatPassword: EditText) {
        var isValidate = true

        if (!phone.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым.")) isValidate = false
        if (!password.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым.")) isValidate = false
        if (!password.validate({ s -> s == repeatPassword.text.toString() }, "Пароли не совпадают.")) isValidate = false


        Log.e("DDDD", isValidate.toString())

        if (isValidate) {

            subscription.add(
                postApi.isUserExist(phone.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .doOnTerminate { hideProgress() }
                    .subscribe(
                        { result ->

                            if (result.isSuccessful) {

                                if (result.body()!!.success == null && result.body()!!.token != null) {

                                    App.activity?.toast("Вы уже зарестрированы в приложении для докторов")
                                    App.activity?.startActivity(
                                        Intent(
                                            App.activity!!,
                                            LoginActivity::class.java
                                        )
                                    )
                                    App.activity?.finish()
                                } else {
                                    if (result.body()!!.success!!) {
                                        val intent = Intent(App.activity, ConfirmCodeActivity::class.java)
                                        intent.putExtra("phone", phone.text.toString())
                                        intent.putExtra("password", password.text.toString())
                                        intent.putExtra("isRegister", 0)
                                        App.activity?.startActivity(intent)
                                        App.activity?.finish()
                                    } else {
                                        App.activity?.toast(result.body()?.error.toString())
                                    }
                                }

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
    }


}