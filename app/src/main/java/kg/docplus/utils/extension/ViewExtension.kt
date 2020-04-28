package kg.docplus.utils.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kg.docplus.R
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.item_doctor.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


fun EditText.validate(
        validator: (String) -> Boolean,
        message: String
): Boolean {
    val flag = validator(this.text.toString())
    this.error = if (flag) null else message

    return flag
}

fun TextView.validate(
        validator: (String) -> Boolean,
        message: String
): Boolean {
    val flag = validator(this.text.toString())
    this.error = if (flag) null else message

    return flag
}

fun EditText.text(
        message: String?
) {
    if (!message.isNullOrEmpty()) {
        this.setText(message)
    }
}

fun TextView.day(
        workingDays: Boolean
) {
    if (workingDays){
        setBackgroundResource(R.drawable.stoke_gray)
        setTextColor(Color.parseColor("#E0E0E0"))
    }else if (!workingDays){
       setBackgroundResource(R.drawable.stoke_blue)
       setTextColor(Color.parseColor("#FDB600"))
    }
}

fun EditText.cursorToEnd(
) {
    setSelection(text.toString().length)
}

fun TextView.text(
        message: String?
) {
    if (!message.isNullOrEmpty()) this.text = message
}

fun EditText.toInteger(
        message: String?
) {
    if (!message.isNullOrEmpty()) {
        this.setText(message)
    }
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG): Toast =
        Toast.makeText(this, message, duration).apply {
            show()
        }

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_LONG): Toast =
        Toast.makeText(this, resId, duration).apply {
            show()
        }

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

@SuppressLint("SimpleDateFormat")
fun dateToPostFormat(date: Date): String {
    val postFormat = SimpleDateFormat("yyyy-MM-dd")
    return postFormat.format(date)
}


@SuppressLint("SimpleDateFormat")
fun dateToChatFormat(date: Date): String {
    val postFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    return postFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun dateToStatusFormat(date: Date): String {
    val postFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    return postFormat.format(date)
}


fun getServiceName(service: Int): String {

    return when (service) {
        0 -> "Записатья на прием"
        1 -> "Онлайн чат"
        2 -> "Видео консультация"
        3 -> "Вызвать на дом"
        else -> "Онлайн чат"
    }

}

@SuppressLint("SimpleDateFormat")
fun getDayOfWeekName(date: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    var day = 0
    try {
        val data = format.parse(date)
        System.out.println(date)
        var calendar = Calendar.getInstance()
        calendar.time = data
        day = calendar.get(Calendar.DAY_OF_WEEK)

    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return when (day) {
        0 -> "Пн"
        1 -> "Вт"
        2 -> "Ср"
        3 -> "Чт"
        4 -> "Пт"
        5 -> "Сб"
        6 -> "Вс"
        else -> "Пн"
    }
}

fun getDayOfWeekName1(date: String): String {
    val format = SimpleDateFormat("dd-MMMM-yyyy")
    var day = 0
    try {
        val data = format.parse(date)
        System.out.println(date)
        var calendar = Calendar.getInstance()
        calendar.time = data
        day = calendar.get(Calendar.DAY_OF_WEEK)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
//    day -= 2
//
//    if (day==-1){
//        day = 6
//    }
    return day.toString()
}

fun getDay(day:Int):String{
    return when (day) {
        0 -> "Понедельник"
        1 -> "Вторник"
        2 -> "Среда"
        3 -> "Четверг"
        4 -> "Пятница"
        5 -> "Суббота"
        6 -> "Воскресенье"
        else -> "Понедельник"
    }
}

fun setRoundedImage(imageView: ImageView, url: String, context: Context) {

    Glide.with(context).load(url)
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(16)))
            .into(imageView)

}

fun setCircleImage(imageView: ImageView, url: String, context: Context) {
    Glide.with(context).load(url)
            .apply(
                    RequestOptions.bitmapTransform(
                            (CropCircleTransformation())
                    )
            ).into(imageView)
}

fun isTime(time: String): Boolean {
    return true
    // var test = "2019-07-08 11:20:00"
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        val data = format.parse(time)
//        val testdata = format.parse(test)
        val testdata = Date()
        val lastDate = Date(data.time + (25 * 60000))

        Log.e("DAtes", format.format(data) + " " + format.format(lastDate))

        return testdata.after(data) && testdata.before(lastDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return false
}

fun isTime(time: Long): Boolean {
    var diff = (Date().time - time)
    //return diff < 25 * 60000
    return true
}

@SuppressLint("SimpleDateFormat")
fun isTimeDoctor(date: String): Boolean {
    var minute25 = 25 * 60000
    val postFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    var date1 = postFormat.parse(date)
    var now = Date()
    if (date1.time - now.time in 1 until minute25) {
        return true
    }

    return false
}

@SuppressLint("SimpleDateFormat")
fun formatDatePreview(date:String):String{

    var date1 = date.substring(0,date.indexOf("."))
    Log.e("DATEEEE",date1)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    var date2 = sdf.parse(date1)
    val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm")

    return sdf1.format(date2)
}


