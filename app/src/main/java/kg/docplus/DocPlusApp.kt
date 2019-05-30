package kg.docplus

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatActivity

class DocPlusApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var context: Context? = null

        @SuppressLint("StaticFieldLeak")
        var activity:AppCompatActivity? = null
    }




    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }



}
