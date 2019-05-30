package kg.docplus.ui.login

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
import kg.docplus.ui.register.RegisterActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

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

                                //UserToken.saveToken(result.body()!!.token, DocPlusApp.context!!)
                                Log.e("TOK",result.body()!!.toString())

                            } else {
                                var error = result.errorBody()!!.string()
                                Log.e("Error",error)

                                if (error.contains("Невозможно войти с",true)){
                                    Log.e("TAF","DDD")
                                    DocPlusApp.activity!!.toast("Невозможно войти с предоставленными учетными данными")
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
        (activity).startActivityForResult(Intent(activity, PostListActivity::class.java), 1)

    }

    fun onClickRegister(view: View) {

        DocPlusApp.activity!!.startActivity(Intent(DocPlusApp.activity,RegisterActivity::class.java))

    }


}