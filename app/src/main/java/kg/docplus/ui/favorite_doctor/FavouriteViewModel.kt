package kg.docplus.ui.favorite_doctor

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.DocPlusApp
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Product
import kg.docplus.model.get.DoctorFull
import kg.docplus.model.get.DoctorGet
import kg.docplus.network.PostApi
import kg.docplus.ui.main.search.DoctorRvAdapter
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import javax.inject.Inject

class FavouriteViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    var adapter = DoctorRvAdapter(DocPlusApp.activity!!)

    private var subscription: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun getDoctorFavourite() {

        subscription.add(
            postApi.getDocFavourite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("DOC_FULL",result.body()!!.toString())
                            adapter.swapData(result.body()!!)

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
        DocPlusApp.activity!!.finish()
    }

}