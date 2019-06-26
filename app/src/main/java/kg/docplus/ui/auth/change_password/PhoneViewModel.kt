package kg.docplus.ui.auth.change_password

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

class PhoneViewModel : BaseViewModel() {

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

    fun onClickPhone(phone: EditText) {
        var isValidate = true

        if (!phone.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым.")) isValidate = false

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

                                    DocPlusApp.activity?.toast("Вы уже зарестрированы в приложении для пациентов")
                                    val intent = Intent(DocPlusApp.activity, ConfirmCodeActivity::class.java)
                                    intent.putExtra("phone", phone.text.toString())
                                    intent.putExtra("isRegister", 1)
                                    DocPlusApp.activity?.startActivity(intent)
                                    DocPlusApp.activity?.finish()
                                } else {
                                    if (result.body()!!.success!!) {
                                        DocPlusApp.activity?.toast("Вы не зарегистрированы")
                                        DocPlusApp.activity?.startActivity(Intent(DocPlusApp.activity!!,RegisterActivity::class.java))
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