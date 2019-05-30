package kg.docplus.injection.module

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import kg.docplus.DocPlusApp
import kg.docplus.utils.UserToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class  HttpClient {

    private val TIMEOUT_VAL: Long = 20000
    private val WRITE_TIMEOUT_VAL: Long = 60000

    fun init(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            if (!isConnected()) {
                return@Interceptor Response.Builder().code(666).build()
            }

            val original = chain.request()


            val request = if (UserToken.hasToken(DocPlusApp.context!!)) {

                original.newBuilder()
                        .method(original.method(), original.body())
                        .addHeader("Authorization", "Token ${UserToken.getToken(DocPlusApp.context!!)}")
                        .build()
            }else{

                original.newBuilder()
                        .method(original.method(), original.body())
                        .build()
            }

            val response = chain.proceed(request)
            Log.d("MyApp", "Code : " + response.code())
            if (response.code() == 401) {

                // Magic is here ( Handle the error as your way )
                Log.e("_________________", "Unauthorized")
                UserToken.clearToken(DocPlusApp.context!!)
                return@Interceptor response
            }
            response
        }).addInterceptor(interceptor)
                .connectTimeout(TIMEOUT_VAL, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_VAL, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT_VAL, TimeUnit.MILLISECONDS)
                .build()
    }

    private fun isConnected() : Boolean {
        val cm = DocPlusApp.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork = cm?.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting ?: true
    }
}
