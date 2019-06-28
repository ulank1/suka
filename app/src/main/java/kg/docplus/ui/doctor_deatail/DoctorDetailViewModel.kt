package kg.docplus.ui.doctor_deatail

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.DoctorFull
import kg.docplus.network.PostApi
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import javax.inject.Inject

class DoctorDetailViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doctor: MutableLiveData<DoctorFull> = MutableLiveData()
    val active: MutableLiveData<Boolean> = MutableLiveData()
    var adapter = ImageRvAdapter(App.activity!!)
    var isActive = false
    var idDoctor:Int=-1

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
                            idDoctor = result.body()!!.id
                            adapter.swapData(result.body()!!.doctor_detail.certificates as ArrayList<String>)
                            isActive = result.body()!!.doctor_detail.is_favorite
                            active.value = isActive
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

    fun onClickMore(line:LinearLayout){
        if (line.visibility==View.GONE){
            line.visible()
        }else{
            line.gone()
        }
    }

    fun onClickHeart(){
        Log.e("IS_SUCCESS",  "$isActive $idDoctor")
        if (isActive){
            subscription.add(
                postApi.deleteFavorite(
                    idDoctor
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .subscribe(
                        { result ->
                            hideProgress()
                            if (result.isSuccessful) {
                                active.value = false
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

            isActive = false
        }else{
            subscription.add(
                postApi.createFavorite(
                    idDoctor
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .subscribe(
                        { result ->
                            hideProgress()
                            if (result.isSuccessful) {
                                active.value = true
                                Log.e("FFDS",result.body()!!.toString())
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
            isActive = true
        }
    }
    fun onClickService(service:Int){
        Log.e("SERVICE",service.toString())
    }

}