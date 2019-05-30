package kg.docplus.ui.main.home

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
import kg.docplus.ui.main.MainActivity
import kg.docplus.ui.main.filter.Filter
import kg.docplus.ui.register.RegisterActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import javax.inject.Inject

class HomeViewModel : BaseViewModel() {

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

    fun onClickCategory(id:Int){

        var service = -1

       when(id){
           R.id.appointment -> service = 1
           R.id.chat -> service = 2
           R.id.video_chat -> service = 3
           R.id.call_home -> service = 4
       }

        (DocPlusApp.activity as MainActivity).selectSearch()

    }
    fun filterDocs(){
        subscription.add(
            postApi.getDocs(
                Filter.min_price,
                Filter.max_price,
                Filter.services,
                Filter.schedule_time_before,
                Filter.schedule_time_after,
                Filter.name,
                Filter.specialty_title,
                Filter.ordering)
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
                        Log.e("DDD",it.toString())}

                )
        )

    }



}