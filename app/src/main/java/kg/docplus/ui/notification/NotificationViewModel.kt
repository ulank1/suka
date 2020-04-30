package kg.docplus.ui.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.Notification
import kg.docplus.model.get.ProfileGet
import kg.docplus.model.get.my_doctor.MyDoctor
import kg.docplus.network.PostApi
import kg.docplus.ui.main.search.DoctorRvAdapter
import javax.inject.Inject

class NotificationViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    private var subscription: CompositeDisposable = CompositeDisposable()
    val myDoctor: MutableLiveData<ArrayList<Notification>> = MutableLiveData()
    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun getNotifications() {

        subscription.add(
            postApi.getNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("NOtif",result.body()!!.toString())
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

    fun tapNot(id:Int) {

        subscription.add(
            postApi.tapNot(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("NOtif11",result.body()!!.toString())
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error11",error)

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