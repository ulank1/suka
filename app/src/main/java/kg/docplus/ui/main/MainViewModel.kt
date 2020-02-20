package kg.docplus.ui.main

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
import javax.inject.Inject

class MainViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    val myDoctor: MutableLiveData<ArrayList<MyDoctor>> = MutableLiveData()


    private var subscription: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }


    fun getDoctorFavourite() {

        subscription.add(
                postApi.getMyDoctors()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        Log.e("DOC_FULL", result.body()!!.toString())
                                        myDoctor.value = result.body()!!
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