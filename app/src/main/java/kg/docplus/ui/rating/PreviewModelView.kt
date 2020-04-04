package kg.docplus.ui.rating

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.my_doctor.MyDoctor
import kg.docplus.network.PostApi
import kg.docplus.ui.main.MainActivity
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.extension.toast
import org.w3c.dom.Comment
import javax.inject.Inject

class PreviewModelView : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    val myDoctor: MutableLiveData<ArrayList<MyDoctor>> = MutableLiveData()


    private var subscription: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }


    fun postPreview(id:Int,rate:Int,comment: String) {

        subscription.add(
                postApi.postPreview(rate,id,comment)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        Log.e("Preview", result.body()!!.toString())
                                    } else {
                                        var error = result.errorBody()!!.string()
                                        Log.e("Error", error)

                                    }

                                },
                                {
                                    hideProgress()

                                    Log.e("DDD", it.toString())
                                }

                        )
        )

    }

    fun getPreview(id:Int) {

        subscription.add(
                postApi.getPreview(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        Log.e("Preview", result.body()!!.toString())
                                    } else {
                                        var error = result.errorBody()!!.string()
                                        Log.e("Error", error)

                                    }

                                },
                                {
                                    hideProgress()

                                    Log.e("DDD", it.toString())
                                }

                        )
        )

    }


}