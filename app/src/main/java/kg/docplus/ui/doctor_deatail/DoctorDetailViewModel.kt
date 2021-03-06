package kg.docplus.ui.doctor_deatail

import android.app.Dialog
import android.content.Intent
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
import kg.docplus.model.get.Cities
import kg.docplus.model.get.DoctorFull
import kg.docplus.model.get.UrlImage
import kg.docplus.model.post.DoctorId
import kg.docplus.network.PostApi
import kg.docplus.ui.main.filter.Filter
import kg.docplus.ui.notification.PayboxActivity
import kg.docplus.ui.rating.RatingActivity
import kg.docplus.utils.extension.*
import kotlinx.android.synthetic.main.fragment_filter.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class DoctorDetailViewModel : BaseViewModel(), DetailListener, AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        date = dateList[position]

        getAvailableTimes(service, dayList[position])
    }

    override fun postAppointment(time: String) {
//        if (service==3){
//            var bool = true
//            if (city.selectedItemPosition==0){
//                App.activity.toast("Выберите город")
//                bool= false
//            }
//
//            if (address.text.toString().isEmpty()){
//                address.error = "Введите адрес"
//                bool = false
//            }
//
//            if (bool){
//                createAppointment(service,time,cities[city.selectedItemPosition].id.toString(),address.text.toString())
//            }
//            createAppointment(service, time)
//
//        }else {
//            createAppointment(service, time)
//        }

        this.time = time

        App.activity.startActivityForResult(Intent(App.activity, PayboxActivity::class.java).putExtra("price", price),1020)

    }

    @Inject
    lateinit var postApi: PostApi
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doctor: MutableLiveData<DoctorFull> = MutableLiveData()
    val active: MutableLiveData<Boolean> = MutableLiveData()
    var adapter = ImageRvAdapter(App.activity!!)
    var adapterTimes = TimesRvAdapter(App.activity!!, this)
    var isActive = false
    lateinit var doctorFull: DoctorFull
    var idDoctor: Int = -1
    var service = 0
    var price = 0
    var time = "00:00"
    lateinit var dialog: Dialog
    lateinit var dialogResult: Dialog
    lateinit var cities: ArrayList<Cities>


    private var subscription: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun getDoctorFull(id: Int) {
        Log.e("ID", id.toString())
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
                                        doctorFull = result.body()!!
                                        doctor.value = result.body()
                                        idDoctor = result.body()!!.id
                                        adapter.swapData(result.body()!!.doctor_detail.certificates as ArrayList<UrlImage>)
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

    fun getCities() {
        subscription.add(
                postApi.getCities()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        Log.e("DOC_FULL", result.body()!!.toString())
                                        cities = (result.body() as ArrayList<Cities>?)!!

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

    fun onClickMore(line: LinearLayout, scrollView: ScrollView, button: Button) {
        if (line.visibility == View.GONE) {
            line.visible()
            scrollView.fullScroll(View.FOCUS_DOWN)
        } else {
            line.gone()
        }
    }

    fun onClickPreview(view: View) {
        view.getParentActivity()?.startActivity(Intent(view.getParentActivity(), RatingActivity::class.java).putExtra("id", idDoctor))
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
            var doc = DoctorId(idDoctor)
            subscription.add(
                    postApi.createFavorite(
                            doc
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

        for (serviceFul in doctorFull.services) {
            if (serviceFul.service==service){
                price = serviceFul.min_price
            }
        }

        showAlert(service)

    }

    var date = ""

    var dateList: ArrayList<String> = ArrayList()
    var dayList: ArrayList<String> = ArrayList()
    lateinit var city: Spinner
    lateinit var address: EditText

    private fun showAlert(service: Int) {
        this.service = service


        dialog = Dialog(App.activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_appointment_create)

        val rv = dialog.findViewById(R.id.rv_times) as RecyclerView
        val title = dialog.findViewById(R.id.service) as TextView
        address = dialog.findViewById(R.id.edit_address) as EditText
        city = dialog.findViewById(R.id.cities) as Spinner
        val date: Spinner = dialog.findViewById(R.id.date)
        var dateListA = ArrayList<String>()
        dateList.clear()
        dayList.clear()
        if (service != 3) {
            address.gone()
            city.gone()
        } else {
            address.gone()
            city.gone()
//            var cityList:ArrayList<String> = ArrayList()
//            cityList.add("Выберите город")
//            for (city1 in cities){
//                cityList.add(city1.name)
//            }
//
//            var adapter = ArrayAdapter(App.activity!!, R.layout.spinner_country_item, cityList)
//            adapter.setDropDownViewResource(R.layout.spinner_country_dropdown_item)
//            city.adapter = adapter
        }


        for (i in 0..13) {
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            var date1 = Date(calendar.timeInMillis)
            val postFormat = SimpleDateFormat("yyyy-MM-dd")
            Log.e("Datesss", postFormat.format(date1) + " " + (calendar.get(Calendar.DAY_OF_WEEK)).toString())
            var day = (calendar.get(Calendar.DAY_OF_WEEK) - 2)
            if (day == -1) {
                day = 6
            }
            dateList.add(postFormat.format(date1))
            dateListA.add(postFormat.format(date1) + " (" + getDayShort(day) + ")")
            dayList.add(day.toString())
        }

        Log.e("DATES", dayList.toString())

        var adapter = ArrayAdapter(App.activity!!, R.layout.spinner_country_item, dateListA)
        adapter.setDropDownViewResource(R.layout.spinner_country_dropdown_item)
        date.adapter = adapter

        date.onItemSelectedListener = this


        title.text = getServiceName(service)

        var manager = GridLayoutManager(App.activity, 3)
        rv.layoutManager = manager
        rv.setHasFixedSize(false)
        rv.adapter = adapterTimes

//        dialog.setCancelable(false)
        dialog.show()

    }


    private fun getAvailableTimes(service: Int, day: String) {
        Log.e("DDD", Filter.date + " " + idDoctor + " " + service)

        subscription.add(
                postApi.getAviableTimes(
                        day,
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

    private fun createAppointment(service: Int, time: String, cityId: String?, address: String?) {
        Log.e("DDD", Filter.date + " " + idDoctor + " " + service)
        subscription.add(
                postApi.postAppointment(
                        service,
                        idDoctor,
                        time,
                        date,
                        cityId,
                        address
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

    fun createAppointment() {
        Log.e("DDdfsdghjkhgfdsD", Filter.date + " " + idDoctor + " " + service)
        subscription.add(
                postApi.postAppointment(
                        service,
                        idDoctor,
                        time,
                        date,
                        "",
                        ""
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
                                        if (error.contains("Расписание на это время уже")) {
                                            App.activity.toast("Расписание на это время уже установлено! Вы можете изменить")
                                        }
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


    private fun showAlertResult(service: Int) {
        this.service = service

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

    fun onClickBack() {
        App.activity.finish()
    }

}