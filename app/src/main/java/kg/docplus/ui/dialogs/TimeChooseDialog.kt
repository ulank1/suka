package kg.docplus.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.NumberPicker
import kg.docplus.R
import kg.docplus.utils.time_picker.TimeChangeListener
import kg.docplus.utils.time_picker.TimePicker


class TimeChooseDialog(context: Context) : Dialog(context),TimeChangeListener{
    override fun onTimeChange(time: String, currentHour: Int, currentMinute: Int) {
        Log.e("TIME",time)

    }
    lateinit var timePicker: TimePicker
    lateinit var timePicker2: TimePicker
    fun setUp() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.item_choose_time)
//        var materialNumberPicker:NumberPicker = findViewById(R.id.num)
//        materialNumberPicker.displayedValues = null
//        materialNumberPicker.minValue = 0
//        materialNumberPicker.maxValue = 3
//        materialNumberPicker.displayedValues = arrayOf("00","30","00","30")
//        materialNumberPicker.setOnValueChangedListener(this)
        timePicker = findViewById(R.id.tp)
        timePicker2 = findViewById(R.id.tp2)
        findViewById<ImageView>(R.id.close).setOnClickListener { hide() }

        timePicker2.setTimePicker(R.layout.item_timer_picker,null,arrayOf("00","30","00","30"), object : TimeChangeListener {
            override fun onTimeChange(time: String, currentHour: Int, currentMinute: Int) {

            }
        })

        timePicker.setTimePicker(R.layout.item_timer_picker,null,arrayOf("00","30","00","30"), object : TimeChangeListener {
            override fun onTimeChange(time: String, currentHour: Int, currentMinute: Int) {
                timePicker2.setTime(currentHour,currentMinute)
            }
        })

    }

    fun getTimeFrom():String{
        return timePicker.getTime()
    }

    fun getTimeTo():String{
        return timePicker2.getTime()
    }

}