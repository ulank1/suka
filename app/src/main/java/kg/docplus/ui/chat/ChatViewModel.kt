package kg.docplus.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.base.BaseViewModel
import kg.docplus.network.PostApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ChatViewModel : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    val avatar: MutableLiveData<String> = MutableLiveData()

    private var subscription: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    fun postImage(path: String) {
        Log.e("LLDD",path)
        var file = File(path)
        val requestBody = RequestBody.create(MediaType.parse("multipart/data"), file)
        val multiPartBody = MultipartBody.Part
            .createFormData("file", file.name, requestBody)

        subscription.add(
            postApi.postImage(multiPartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("IMAGEDS", result.body()!!.file)
                            avatar.value = result.body()!!.file
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