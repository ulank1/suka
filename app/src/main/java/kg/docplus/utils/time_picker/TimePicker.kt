package kg.docplus.utils.time_picker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.NumberPicker
import kg.docplus.R
import java.util.*

class TimePicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var np1:NumberPicker
    lateinit var np2:NumberPicker
    lateinit var listener:TimeChangeListener
    var fromHour = -1
    var fromMinute = -1
    var hour = "00"
    var minute = "00"


    fun setTimePicker(layout_id:Int,hours:Array<String>?,minutes:Array<String>?,listener:TimeChangeListener){
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(layout_id, this)

        this.listener = listener

        np1 = findViewById(R.id.np1)
        np2 = findViewById(R.id.np2)
        np1.setFormatter { String.format("%02d", it) }


        if (hours.isNullOrEmpty()){
            np1.minValue = 0
            np1.maxValue = 23
        }else{
            np1.displayedValues = null
            np1.minValue = 0
            np1.maxValue = hours.size-1
            np1.displayedValues = hours
        }

        if (minutes.isNullOrEmpty()){
            np2.minValue = 0
            np2.maxValue = 59
        }else{
            np2.displayedValues = null
            np2.minValue = 0
            np2.maxValue = minutes.size-1
            np2.displayedValues = minutes
        }
        setCurrentTime()

        np1.setOnValueChangedListener { picker, oldVal, newVal ->
            hour = if (picker.displayedValues.isNullOrEmpty()){
                String.format("%02d", picker.value)
            }else {
                picker.displayedValues[newVal]
            }
            setChangeTime()
        }
        np2.setOnValueChangedListener { picker, oldVal, newVal ->

            minute = if (picker.displayedValues.isNullOrEmpty()) {
                String.format("%02d", picker.value)
            } else {
                picker.displayedValues[newVal]
            }
            setChangeTime()
        }

    }



    private fun setChangeTime() {
        listener.onTimeChange("$hour:$minute",np1.value,np2.value)
        setTrueMinute()
    }

    private fun setTrueMinute(){
        if (fromHour!=-1){
            if (fromMinute%2==0&&fromHour == np1.value){
                np2.displayedValues = null
                np2.minValue = 0
                np2.maxValue = 3
                np2.displayedValues = arrayOf("30","30","30","30")

            }else{
                np2.displayedValues = null
                np2.minValue = 0
                np2.maxValue = 3
                np2.displayedValues = arrayOf("00","30","00","30")
            }
        }
    }

    fun getTime():String{
        return "$hour:$minute"
    }

    fun setTime(currentHour: Int, currentMinute: Int){

        fromHour = currentHour
        fromMinute = currentMinute

        if (currentMinute%2==0) {
            np1.minValue = currentHour
            np1.value = currentHour
            np2.value = 1
        }else{
            np1.minValue = currentHour+1
            np1.value = currentHour+1
            np2.value = currentMinute+1
        }
        setTrueMinute()
    }

    private fun setCurrentTime(){
        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        if (minute/30<1){
            this.minute = "00"
            np1.value = hour
            np2.value = 1
        }else{
            this.minute = "30"
            np1.value = hour+1
            np2.value = 0
        }
        if (hour<10){
            this.hour = "0$hour"
        }else{
            this.hour = hour.toString()
        }

        listener.onTimeChange("$hour:$minute",np1.value,np2.value)

        Log.e("HOUR+MINUTE", "$hour $minute ${np1.value}")
    }


}