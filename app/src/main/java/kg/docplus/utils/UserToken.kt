package kg.docplus.utils

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import android.util.Log

private val TAG = UserToken::class.java.simpleName
object UserToken{
    val APP_PREFERENCES = "mysettings"
    val TOKEN = "tokenID"
    val PROFILE = "profile"


    private var mSettings: SharedPreferences? = null

    fun saveToken(token: String, context: Context) {

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val editor = mSettings!!.edit()
        editor.putString(TOKEN, token)
//        Log.e(TAG, "saved $token")
        editor.apply()
    }

    fun getToken(context: Context): String? {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        return mSettings!!.getString(TOKEN, "052e7e1af46dd1cf0c9c4b5243a57f57e0b4b6c4")
    }

    fun clearToken(context: Context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = mSettings!!.edit().clear()
        editor.apply()
    }

    fun hasToken(context: Context): Boolean {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val token = mSettings!!.getString(TOKEN, "empty")
//        Log.e(TAG, token)
       // return token != "empty" && token != ""
        return true
    }
    fun saveProfile(token: String, context: Context) {

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val editor = mSettings!!.edit()
        editor.putString(PROFILE, token)
//        Log.e(TAG, "saved $token")
        editor.apply()
    }
}