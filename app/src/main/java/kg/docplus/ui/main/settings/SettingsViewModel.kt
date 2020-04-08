package kg.docplus.ui.main.settings

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.App
import kg.docplus.base.BaseViewModel
import kg.docplus.network.PostApi
import kg.docplus.utils.UserToken
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class SettingsViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi

    val term: MutableLiveData<String> = MutableLiveData()
    val sendFail: MutableLiveData<Boolean> = MutableLiveData()

    private var subscription: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun postReport(message: String) {

        subscription.add(
                postApi.postReport(message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {

                                       sendFail.value = true


                                    } else {
                                        var error = result.errorBody()!!.string()
                                        Log.e("Error", error)
                                        sendFail.value = false


                                    }

                                },
                                {
                                    hideProgress()
                                    sendFail.value = false

                                    Log.e("DDD", it.toString())
                                }

                        )
        )

    }

    fun postSupport(message: String) {

        subscription.add(
                postApi.postSupport(message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        sendFail.value = true

                                    } else {
                                        var error = result.errorBody()!!.string()
                                        Log.e("Error", error)
                                        sendFail.value = false

                                    }

                                },
                                {
                                    hideProgress()
                                    sendFail.value = false

                                    Log.e("DDD", it.toString())
                                }

                        )
        )

    }
    fun getTerms() {

        subscription.add(
                postApi.getTerms()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { showProgress() }
                        .subscribe(
                                { result ->
                                    hideProgress()
                                    if (result.isSuccessful) {
                                        term.value = result.body()?.body.toString()
                                    } else {
                                        var error = result.errorBody()!!.string()
                                        Log.e("Error", error)

                                    }

                                },
                                {
                                    hideProgress()

                                    Log.e("DDD", it.toString())
                                }

                        )
        )

    }

}