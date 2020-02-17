package kg.docplus.utils.time_picker

interface TimeChangeListener {

    fun onTimeChange(time:String,currentHour:Int,currentMinute:Int)

}