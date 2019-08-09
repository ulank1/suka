package kg.docplus.ui.doctor_deatail

import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.DoctorFull
import kg.docplus.network.PostApi
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.extension.getServiceName
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.visible
import javax.inject.Inject


class DoctorDetailViewModel : BaseViewModel(), DetailListener {
    override fun postAppointment(time: String) {
        createAppointment(service, time)
    }

    @Inject
    lateinit var postApi: PostApi
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doctor: MutableLiveData<DoctorFull> = MutableLiveData()
    val active: MutableLiveData<Boolean> = MutableLiveData()
    var adapter = ImageRvAdapter(App.activity!!)
    var adapterTimes = TimesRvAdapter(App.activity!!, this)
    var isActive = false
    var idDoctor: Int = -1
    var service = 0
    lateinit var dialog:Dialog
    lateinit var dialogResult:Dialog



    private var subscription: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun getDoctorFull(id: Int) {
        Log.e("ID",id.toString())
        idDoctor = id
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
                            Log.e("DOC_FULL", result.body()!!.toString())
                            doctor.value = result.body()
                            idDoctor = result.body()!!.id
                            adapter.swapData(result.body()!!.doctor_detail.certificates as ArrayList<String>)
                            isActive = result.body()!!.doctor_detail.is_favorite
                            active.value = isActive
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

    fun onClickMore(line: LinearLayout,scrollView: ScrollView,button: Button) {
        if (line.visibility == View.GONE) {
            line.visible()
            scrollView.fullScroll(View.FOCUS_DOWN)
        } else {
            line.gone()
        }
    }

    fun onClickHeart() {
        Log.e("IS_SUCCESS", "$isActive $idDoctor")
        if (isActive) {
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
                                Log.e("Error", error)

                            }

                        },
                        {
                            hideProgress()
                            active.value = false

                            Log.e("DDD", it.toString())
                        }

                    )
            )

            isActive = false
        } else {
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
                                Log.e("FFDS", result.body()!!.toString())
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
            isActive = true
        }
    }

    fun onClickService(service: Int) {
        Log.e("SERVICE", service.toString())

        showAlert(service)

    }


    private fun showAlert(service: Int) {
        this.service = service
        getAvailableTimes(service)

        dialog = Dialog(App.activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_appointment_create)

        val rv = dialog.findViewById(R.id.rv_times) as RecyclerView
        val title = dialog.findViewById(R.id.service) as TextView

        title.text = getServiceName(service)

        var manager = GridLayoutManager(App.activity, 3)
        rv.layoutManager = manager
        rv.setHasFixedSize(false)
        rv.adapter = adapterTimes

//        dialog.setCancelable(false)
        dialog.show()

    }

    private fun getAvailableTimes(service: Int) {
        Log.e("DDD", Filter.date + " " + idDoctor + " " + service)
        subscription.add(
            postApi.getAviableTimes(
                Filter.date,
                idDoctor,
                service
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { }
                .subscribe(
                    { result ->

                        if (result.isSuccessful) {
                            Log.e("TIMES", result.body()!!.toString())
                            adapterTimes.swapData(result.body()!!.free_time)
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

    }

    private fun createAppointment(service: Int, time: String) {
        Log.e("DDD", Filter.date + " " + idDoctor + " " + service)
        subscription.add(
            postApi.postAppointment(
                service,
                idDoctor,
                time,
                Filter.date
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("TIMES", result.body()!!.toString())
                            dialog.cancel()
                            showAlertResult(service)
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error", error)
                            App.activity.toast(error)

                        }

                    },
                    {
                        hideProgress()
                        Log.e("DDD", it.toString())
                    }

                )
        )

    }

    private fun showAlertResult(service: Int) {
        this.service = service
        getAvailableTimes(service)

        dialogResult = Dialog(App.activity!!)
        dialogResult.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogResult.setCancelable(true)
        dialogResult.setContentView(R.layout.alert_result)

        val title = dialogResult.findViewById(R.id.service) as TextView
        val text = dialogResult.findViewById(R.id.text) as TextView
        val btnOk = dialogResult.findViewById(R.id.btn_ok) as Button

        title.text = getServiceName(service)

        text.text = "Ваш запрос ${getServiceName(service)} успешно отправлен."

        btnOk.setOnClickListener { dialogResult.cancel() }
//        dialog.setCancelable(false)
        dialogResult.show()

    }
    fun onClickBack(){
        App.activity.finish()
    }

}