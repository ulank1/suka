package kg.docplus.utils.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

fun View.getParentActivity(): AppCompatActivity?{
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

fun Activity.hideKeyboard(){
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}