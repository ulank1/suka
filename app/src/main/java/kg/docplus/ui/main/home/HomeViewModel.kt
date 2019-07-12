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
           R.id.appointment -> service = 1
           R.id.chat -> service = 2
           R.id.video_chat -> service = 3
           R.id.call_home -> service = 4
       }

        (App.activity as MainActivity).selectSearch()

    }

}