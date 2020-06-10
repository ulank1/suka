package kg.docplus.ui.main.search


import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.TextView
import com.cjj.MaterialRefreshLayout
import com.cjj.MaterialRefreshListener

import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kg.docplus.ui.main.MainActivity
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import kotlinx.android.synthetic.main.fragment_filter.*
import java.text.SimpleDateFormat
import java.util.*
import kg.docplus.utils.extension.hideKeyboard
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kg.docplus.App
import kg.docplus.model.get.Specialties
import kg.docplus.ui.dialogs.TimeChooseDialog
import kg.docplus.utils.extension.getDay
import kg.mvvmdordoi.ui.game.PaginationScrollListener
import kotlinx.android.synthetic.main.activity_show_image.*
import kotlin.text.Typography.times


// TODO: Rename parameter arguments, choose names that match

class FilterFragment : Fragment(), View.OnClickListener, TextWatcher, FilterListener, AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        activity?.hideKeyboard()
        if (view != null) {
        Log.e("IDDFF",view.id.toString()+"  "+R.id.speciality_spinner+"  "+parent?.id.toString())
        Log.e("IDDFF",view.id.toString()+"  "+R.id.spinner+"  "+id.toString())
            if (parent != null) {
                if (parent.id==R.id.spinner) {
                    if (position == 0) {
                        Filter.ordering = "schedule__starts_at_time"
                    } else {
                        Filter.ordering = "schedule__services__price"
                    }
                    adapterResult.clearData()
                    viewModel.filterDocs()
                }else if (parent.id==R.id.speciality_spinner){
                    if (position>0) {
                        Filter.specialty_title = specialties_spinner[position - 1].id.toString()
                    }
                }

            }
        }
    }

    override fun choose(speciality: String?,id:Int) {
        Filter.specialty_title = id.toString()
        Filter.name = null

        for ((i,spec) in specialties_spinner.withIndex()){
            if (id==spec.id){
                speciality_spinner.setSelection(i+1)
                break
            }
        }

        edit_search.setText(speciality)
        changeStatus(2)
        activity!!.hideKeyboard()
        viewModel.filterDocs()

    }

    override fun afterTextChanged(s: Editable?) {
        changeStatus(1)
        viewModel.pageOfFilterDropDown = "1"
        specialties.clear()
        viewModel.filterSpeciality(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    lateinit var adapter: SpecialityRvAdapter
    lateinit var adapterResult: DoctorRvAdapter

    private lateinit var viewModel: FilterViewModel
    var lateStatus = 0

    var specialties: ArrayList<Specialties> = ArrayList()
    var specialties_spinner: ArrayList<Specialties> = ArrayList()

    var dateList: ArrayList<String> = ArrayList()
    var specialityId:Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        Filter.date = ""
        Filter.max_price = null
        Filter.min_price = 0
        Filter.schedule_time_after = null
        Filter.schedule_time_before = null
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(FilterViewModel::class.java)
        viewModel.specialities.observe(this, Observer {
            specialties.addAll(it!!)
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this, Observer { setupStatus(it!!) })
        viewModel.doctors.observe(this, Observer {
            adapterResult.swapData(it.results!!)
        })
        viewModel.specialities_spinner.observe(this, Observer {
            specialties_spinner = it
            var ss:ArrayList<String> = ArrayList()
            ss.add("Специальность")
            for(s in it){
                ss.add(s.title)
            }
            val adapter = ArrayAdapter(context, R.layout.spinner,ss)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            speciality_spinner.adapter = adapter
            speciality_spinner.onItemSelectedListener = this


        })

        Log.e("Token", UserToken.getToken(activity!!))
        return view
    }

    var times: ArrayList<String> = ArrayList()
    lateinit var timeChooseDialog:TimeChooseDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatus(1)
        viewModel.pageOfAllDropDown = "1"
        viewModel.pageOfFilterDropDown = "1"
        viewModel.getAllDropdown()
        viewModel.getSpeciality()
        initDateSpinner()

        setOnClickListeners()
        edit_search.addTextChangedListener(this)
        date.setOnTouchListener { v, event ->
            activity?.hideKeyboard()
            return@setOnTouchListener false
        }
        speciality_spinner.setOnTouchListener { v, event ->
            activity?.hideKeyboard()
            return@setOnTouchListener false
        }
        setupRv()
        viewModel.filterSpeciality("")
        val adapter = ArrayAdapter.createFromResource(activity, R.array.sort, R.layout.spinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        val choosen = arguments!!.getInt("service")
        Log.e("CHOOSEN", choosen.toString())
        when (choosen) {
            0 -> appointment.isChecked = true
            1 -> chat.isChecked = true
            2 -> video_chat.isChecked = true
            3 -> call_home.isChecked = true
        }
        timeChooseDialog = TimeChooseDialog(context!!)
        timeChooseDialog.setUp()


    }

    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    private fun setupRv() {
        refresh.setMaterialRefreshListener(object : MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout) {
                materialRefreshLayout.finishRefreshing()
                materialRefreshLayout.finishRefresh()
            }

            override fun onRefreshLoadMore(materialRefreshLayout: MaterialRefreshLayout) {

            }
        })
        rv.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        adapter = SpecialityRvAdapter(activity!!, this)
        rv.adapter = adapter
        adapter.swapData(specialties)

        rv_result.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        adapterResult = DoctorRvAdapter(activity!!)
        rv_result.adapter = adapterResult
//        rv_result.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
//            override fun isLastPage(): Boolean {
//                return isLastPage
//            }
//
//            override fun isLoading(): Boolean {
//                return isLoading
//            }
//
//            override fun loadMoreItems() {
//                isLoading = true
//                //you have to call loadmore items to get more data
//                viewModel.search(page)
//            }
//        })

    }

    private fun setOnClickListeners() {
        btn_ok.setOnClickListener(this)
        time.setOnClickListener(this)
        btn_filter.setOnClickListener(this)
        btn_search.setOnClickListener(this)
        scroll.setOnClickListener(this)
        back.setOnClickListener {
            (activity as MainActivity).onBackFromFragment()
        }
    }

    fun onBack(){
        if (lateStatus > 1) {
            changeStatus(1)
        } else {
            (activity as MainActivity).onBackFromFragment()
        }
    }


    private fun setupStatus(status: Int) {

        if (status != lateStatus) {
            lateStatus = status

            when (status) {
                1 -> {
                    Filter.name = ""
                    Filter.specialty_title=""
                    adapterResult.clearData()
                    refresh.visible()
                    scroll.gone()
                    line_result.gone()
                    search_line.visible()
                    btn_filter.gone()
                }
                2 -> {
                    refresh.gone()
                    scroll.gone()
                    line_result.visible()
                    search_line.visible()
                    btn_filter.visible()
                }
                3 -> {
                    refresh.gone()
                    scroll.visible()
                    line_result.gone()
                    search_line.gone()
                }
            }

        }
    }

    private fun changeStatus(status: Int) {
        viewModel.status.value = status
    }



    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        activity?.hideKeyboard()
        if (v != null) {
            when (v.id) {
                R.id.btn_ok -> {

                    Filter.services = ArrayList()

                    if (appointment.isChecked) {
                        Filter.services.add(0)
                        Filter.service = 0
                    }

                    if (chat.isChecked) {
                        Filter.services.add(1)
                        Filter.service = 1
                    }

                    if (video_chat.isChecked) {
                        Filter.services.add(2)
                        Filter.service = 2
                    }

                    if (call_home.isChecked) {
                        Filter.services.add(3)
                        Filter.service = 3
                    }

                    if (min_price.text.toString().isNotEmpty()) {
                        Filter.min_price = min_price.text.toString().toInt()
                    } else {
                        Filter.min_price = 0
                    }

                    if (max_price.text.toString().isNotEmpty()) {
                        Filter.max_price = max_price.text.toString()
                    } else {
                        Filter.max_price = null
                    }

                    Filter.date = dateList[date.selectedItemPosition]

                    viewModel.filterDocs()
                    changeStatus(2)

                }
                R.id.time -> {

                    timeChooseDialog.show()
                    timeChooseDialog.findViewById<Button>(R.id.btn_ok).setOnClickListener {
                        Filter.schedule_time_after = timeChooseDialog.getTimeFrom()
                        Filter.schedule_time_before = timeChooseDialog.getTimeTo()
                        time.text = "с ${timeChooseDialog.getTimeFrom()} до ${timeChooseDialog.getTimeTo()}"
                        timeChooseDialog.hide()
                    }

                }
                R.id.btn_search -> {
                    Filter.name = edit_search.text.toString()
                    Filter.specialty_title = null
                    changeStatus(2)
                    activity!!.hideKeyboard()
                    viewModel.filterDocs()

                }
                R.id.btn_filter -> {

                    changeStatus(3)
                }
            }
        }
    }


    private fun showAlertTime(text: TextView) {
        var dialog = Dialog(context)
        var listener = DetailListener { _time ->
            text.text = _time
            dialog.cancel()
        }

        var adapterTimes = AlertTimesRvAdapter(context!!, listener)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.item_alert_time)
        val rv = dialog.findViewById(R.id.rv_times) as RecyclerView
        var manager = GridLayoutManager(context, 3)
        rv.layoutManager = manager
        rv.setHasFixedSize(false)
        rv.adapter = adapterTimes
        adapterTimes.swapData(times)
        dialog.show()
    }

    private fun initDateSpinner() {
        dateList.add("")
        var dayOfWeek = ArrayList<String>()
        dayOfWeek.add("Выберите дату")
        for (i in 0..13) {
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            var date = Date(calendar.timeInMillis)
            val postFormat = SimpleDateFormat("dd-MMMM-yyyy")
            var dateName = getMonthName(postFormat.format(date))

            dateList.add(dateName)
            var day = (calendar.get(Calendar.DAY_OF_WEEK)-2)
            if (day==-1){
                day = 6
            }
            dayOfWeek.add(getDay(day))
        }

        Log.e("DATES", dateList.toString())

        var adapter = CustomDropDownAdapter(context!!,dateList,dayOfWeek)
        date.adapter = adapter

    }

    private fun getMonthName(s: String): String {
//        s.replace("January","Января")
//        s.replace("February","Февраля")
//        s.replace("March","Марта")
//        s.replace("April","Апреля")
//        s.replace("May","Мая")
//        s.replace("June","Июня")
//        s.replace("July","Июля")
//        s.replace("August","Августа")
//        s.replace("September","Сентября")
//        s.replace("October","Октября")
//        s.replace("November","Ноября")
//        s.replace("December","Декабря")
        return s
    }


}
