package kg.docplus.ui.main.search

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.DoctorGet
import kg.docplus.network.PostApi
import kg.docplus.ui.main.filter.Filter
import javax.inject.Inject

class FilterViewModel : BaseViewModel() {



    @Inject
    lateinit var postApi: PostApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val status: MutableLiveData<Int> = MutableLiveData()
    val specialities: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val doctors: MutableLiveData<ArrayList<DoctorGet>> = MutableLiveData()
    var pageOfAllDropDown = "1"
    var pageOfFilterDropDown = "1"

    private var subscription: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun filterSpeciality(filter:String){

        subscription.add(
            postApi.getDropDownFilter(
                pageOfFilterDropDown.toString(),
                filter
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
                .subscribe(
                    { result ->
                        loadingVisibility.value = View.GONE
                        if (result.isSuccessful) {
                            Log.e("TOK",result.body()!!.toString())
                            specialities.value = result.body()!!.results
                            pageOfFilterDropDown = result.body()!!.next
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error",error)

                        }

                    },
                    {
                        Log.e("DDD",it.toString())}

                )
        )
    }

    fun filterDocs() {
        Log.e("DATE", Filter.schedule_time_after + " " + Filter.schedule_time_before)
        Log.e("Filter", "${Filter.min_price} ${Filter.max_price} ${Filter.services} ${Filter.name} ")
        subscription.add(
            postApi.getDocs(
                Filter.min_price,
                Filter.max_price,
                Filter.service,
                Filter.schedule_time_before,
                Filter.schedule_time_after,
                Filter.specialty_title,
                Filter.date
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            doctors.value = (result.body() as ArrayList<DoctorGet>?)!!
                            Log.e("TOK", result.body()!!.toString())

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error", error)

                        }

                    },
                    {
                        Log.e("DDD", it.toString())
                    }

                )
        )
        /* subscription.add(
            postApi.getDocsTest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            doctors.value = (result.body() as ArrayList<DoctorGet>?)!!
                            Log.e("TOKLLLLLL",result.body()!!.toString())

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error",error)

                        }

                    },
                    {
                        Log.e("DDD",it.toString())}

                )
        )
    }*/
    }

    fun getAllDropdown() {
        subscription.add(
            postApi.getDropdown(
                pageOfAllDropDown
               )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate {  }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            specialities.value = result.body()!!.results
                            pageOfAllDropDown = result.body()!!.next
                            Log.e("TOK",result.body()!!.toString())
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error",error)

                        }

                    },
                    {
                        Log.e("DDD",it.toString())}

                )
        )
    }




}