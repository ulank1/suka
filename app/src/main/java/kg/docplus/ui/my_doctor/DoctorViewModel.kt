package kg.docplus.ui.my_doctor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.ProfileGet
import kg.docplus.model.get.my_doctor.MyDoctor
import kg.docplus.network.PostApi
import kg.docplus.ui.main.search.DoctorRvAdapter
import javax.inject.Inject

class DoctorViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    private var subscription: CompositeDisposable = CompositeDisposable()
    val myDoctor: MutableLiveData<ArrayList<MyDoctor>> = MutableLiveData()
   // val token: MutableLiveData<String> = MutableLiveData()
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
                            Log.e("DOC_FULL",result.body()!!.toString())
                            myDoctor.value = result.body()!!
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

    fun onClickBack(){
        App.activity!!.finish()
    }

}