package kg.docplus.ui.doctor_deatail

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
import kg.docplus.model.get.DoctorFull
import kg.docplus.network.PostApi
import kg.docplus.post.PostListActivity
import kg.docplus.ui.register.RegisterActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class DoctorDetailViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    var postList: ArrayList<Product> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doctor: MutableLiveData<DoctorFull> = MutableLiveData()
    val active: MutableLiveData<Boolean> = MutableLiveData()

    private var subscription: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun getDoctorFull(id: Int) {


        subscription.add(
            postApi.getDocFull(
               id
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("DOC_FULL",result.body()!!.toString())
                            doctor.value = result.body()
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error",error)

                        }

                    },
                    {
                        hideProgress()

                        Log.e("DDD",it.toString())}

                )
        )

    }

}