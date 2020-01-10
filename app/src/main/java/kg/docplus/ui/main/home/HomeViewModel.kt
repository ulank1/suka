package kg.docplus.ui.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.network.PostApi
import kg.docplus.ui.main.MainActivity
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.extension.toast
import javax.inject.Inject

class HomeViewModel : BaseViewModel() {

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

    fun onClickCategory(id:Int){

        var service = -1

       when(id){
           R.id.appointment -> service = 0
           R.id.chat -> service = 1
           R.id.video_chat -> service = 2
           R.id.call_home -> service = 3
       }

        (App.activity as MainActivity).selectSearch(service)

    }

    fun getNotCounts() {

        subscription.add(
                postApi.getNotCount()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        loadingVisibility.value = result.body()?.notifications_count
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