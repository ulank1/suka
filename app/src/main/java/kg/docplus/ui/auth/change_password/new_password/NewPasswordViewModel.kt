package kg.docplus.ui.auth.change_password.new_password

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.network.PostApi
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class NewPasswordViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
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

                                App.activity!!.toast("Пароль изменен")

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