package kg.docplus.ui.main.search


import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kg.docplus.DocPlusApp


// TODO: Rename parameter arguments, choose names that match

class FilterFragment : Fragment(), View.OnClickListener, TextWatcher, FilterListener,AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position==0) {
            Filter.ordering = "schedule__starts_at_time"
        }else{
            Filter.ordering = "schedule__services__price"
        }
        adapterResult.clearData()
        viewModel.filterDocs()
    }

    override fun choose(speciality: String?) {
        Filter.name = speciality
        Filter.specialty_title = speciality
        edit_search.setText(speciality)
        changeStatus(2)
        activity!!.hideKeyboard()
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
    lateinit var adapterResult:DoctorRvAdapter

    private lateinit var viewModel: FilterViewModel
    var lateStatus = 0

    var specialties:ArrayList<String> = ArrayList()

    var dateList:ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(FilterViewModel::class.java)

        viewModel.specialities.observe(this,android.arch.lifecycle.Observer {
            specialties.addAll(it!!)
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this,android.arch.lifecycle.Observer { setupStatus(it!!) })
        viewModel.loadingVisibility.observe(this,android.arch.lifecycle.Observer { progress.visibility = it!! })
        viewModel.doctors.observe(this,android.arch.lifecycle.Observer { adapterResult.swapData(it!!)
        changeStatus(3)})

        Log.e("Token",UserToken.getToken(activity!!))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatus(1)
        viewModel.pageOfAllDropDown = "1"
        viewModel.pageOfFilterDropDown = "1"
        viewModel.getAllDropdown()
        setOnClickListeners()
        edit_search.addTextChangedListener(this)
        setupRv()
        viewModel.filterSpeciality("")
        val adapter = ArrayAdapter.createFromResource(activity, R.array.sort, R.layout.spinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    private fun setupRv() {
        refresh.setMaterialRefreshListener(object:MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout) {
                materialRefreshLayout.finishRefreshing()
                materialRefreshLayout.finishRefresh()
            }
            override fun onRefreshLoadMore(materialRefreshLayout:MaterialRefreshLayout) {
                if (edit_search.text.toString().isNotEmpty()){
                    viewModel.filterSpeciality(edit_search.text.toString())
                }else {
                    viewModel.getAllDropdown()
                }
            }
        })
        rv.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        adapter = SpecialityRvAdapter(activity!!, this)
        rv.adapter = adapter
        adapter.swapData(specialties)

        rv_result.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        adapterResult = DoctorRvAdapter(activity!!)
        rv_result.adapter = adapterResult

    }

    private fun setOnClickListeners() {
        btn_ok.setOnClickListener(this)
        from_time.setOnClickListener(this)
        to_time.setOnClickListener(this)
        back.setOnClickListener {
            if (lateStatus>1){
                changeStatus(1)
            }else{
                (activity as MainActivity).onBackFromFragment()
            }
        }
    }

    private fun setupStatus(status:Int){
        if (status!=lateStatus){
            lateStatus = status

            when(status){
                1->{
                    adapterResult.clearData()
                    refresh.visible()
                    line_filter.gone()
                    line_result.gone()
                }
                2->{
                    initDateSpinner()
                    refresh.gone()
                    line_filter.visible()
                    line_result.gone()
                }
                3->{
                    refresh.gone()
                    line_filter.gone()
                    line_result.visible()
                }
            }

        }
    }
    private fun changeStatus(status:Int){
        viewModel.status.value = status
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_ok -> {

                    Filter.services = ArrayList()

                    if (appointment.isChecked){
                        Filter.services.add(0)
                    }

                    if (chat.isChecked){
                        Filter.services.add(1)
                    }

                    if (video_chat.isChecked){
                        Filter.services.add(2)
                    }

                    if (call_home.isChecked){
                        Filter.services.add(3)
                    }

                    if (min_price.text.toString().isNotEmpty()){
                        Filter.min_price = min_price.text.toString().toInt()
                    }else{
                        Filter.min_price = 0
                    }

                    if (max_price.text.toString().isNotEmpty()){
                        Filter.max_price = max_price.text.toString().toInt()
                    }else{
                        Filter.max_price = 100000
                    }

                    Filter.schedule_time_before = "schedule_time_before ${from_time.text.toString()}"
                    Filter.schedule_time_after = "schedule_time_after ${to_time.text.toString()}"

                    Filter.date = dateList[date.selectedItemPosition]

                    viewModel.filterDocs()

                }
                R.id.from_time -> {

                    val cal = Calendar.getInstance()
                    val timeSetListener = TimePickerDialog.OnTimeSetListener { time_picker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        (v as TextView).text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                    TimePickerDialog(
                        activity,
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()

                }
                R.id.to_time -> {

                    val cal = Calendar.getInstance()
                    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        (v as TextView).text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                    TimePickerDialog(
                        activity,
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()

                }
            }
        }
    }

    private fun  initDateSpinner() {

        for (i in 0..6){
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR,i)
            var date = Date(calendar.timeInMillis)
            val postFormat = SimpleDateFormat("yyyy-MM-dd")
            dateList.add(postFormat.format(date))
        }

        Log.e("DATES",dateList.toString())

        var adapter = ArrayAdapter(DocPlusApp.activity!!, R.layout.spinner_country_item, dateList)
        adapter.setDropDownViewResource(R.layout.spinner_country_dropdown_item)
        date.adapter = adapter

    }


}
