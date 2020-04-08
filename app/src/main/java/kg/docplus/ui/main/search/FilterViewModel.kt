package kg.docplus.ui.main.search

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Paginate
import kg.docplus.model.get.DoctorGet
import kg.docplus.model.get.Specialties
import kg.docplus.network.PostApi
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.extension.getDayOfWeekName
import kg.docplus.utils.extension.getDayOfWeekName1
import javax.inject.Inject

class FilterViewModel : BaseViewModel() {



    @Inject
    lateinit var postApi: PostApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val status: MutableLiveData<Int> = MutableLiveData()
    val specialities: MutableLiveData<ArrayList<Specialties>> = MutableLiveData()
    val specialities_spinner: MutableLiveData<ArrayList<Specialties>> = MutableLiveData()
    val doctors: MutableLiveData<Paginate<DoctorGet>> = MutableLiveData()
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
                            specialities.value = result.body()!!
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

    fun getSpeciality(){

        subscription.add(
                postApi.getDropDownFilter(
                        pageOfFilterDropDown.toString(),
                        ""
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
                        .subscribe(
                                { result ->
                                    loadingVisibility.value = View.GONE
                                    if (result.isSuccessful) {
                                        Log.e("TOK",result.body()!!.toString())
                                        specialities_spinner.value = result.body()!!
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
        if (Filter.date=="гггг-MM-дд"){
            Filter.date=null
        }
        var day:String? = ""
        if (!Filter.date.isNullOrEmpty()){
            day = getDayOfWeekName1(Filter.date!!)
        }

        Log.e("DATE", Filter.schedule_time_after + " " + Filter.schedule_time_before)
        Log.e("Filter", "${Filter.min_price} ${Filter.max_price} ${Filter.services} ${Filter.name} ")
        Log.e("FILTER",Filter.ttt())
        Log.e("GGGTTYYUI",day.toString())
        subscription.add(
            postApi.getDocs(
                Filter.min_price,
                Filter.max_price,
                Filter.schedule_time_after,
                Filter.schedule_time_before,
//                Filter.specialty_title,
                Filter.name,
                day
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            doctors.value = result.body()!!
                            Log.e("TOKDOC", result.body()!!.toString())

                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("ErrorDOC", error)

                        }

                    },
                    {
                        Log.e("DDDDOC", it.toString())
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
                            //specialities.value = result.body()!!.results
                            //pageOfAllDropDown = result.body()!!.next
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