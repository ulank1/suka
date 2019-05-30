package kg.docplus.ui.register

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Product
import kg.docplus.network.PostApi
import kg.docplus.post.PostListActivity
import kg.docplus.ui.login.LoginActivity
import kg.docplus.ui.register.confirm_code.ConfirmCodeActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    var postList: ArrayList<Product> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()


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

                                    DocPlusApp.activity?.toast("Вы уже зарестрированы в приложении для докторов")
                                    DocPlusApp.activity?.startActivity(
                                        Intent(
                                            DocPlusApp.activity!!,
                                            LoginActivity::class.java
                                        )
                                    )
                                    DocPlusApp.activity?.finish()
                                } else {
                                    if (result.body()!!.success!!) {
                                        val intent = Intent(DocPlusApp.activity, ConfirmCodeActivity::class.java)
                                        intent.putExtra("phone", phone.text.toString())
                                        intent.putExtra("password", password.text.toString())
                                        intent.putExtra("isRegister", true)
                                        DocPlusApp.activity?.startActivity(intent)
                                        DocPlusApp.activity?.finish()
                                    } else {
                                        DocPlusApp.activity?.toast(result.body()?.error.toString())
                                    }
                                }

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