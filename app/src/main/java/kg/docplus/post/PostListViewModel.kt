package kg.docplus.post

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Post
import kg.docplus.network.PostApi
import javax.inject.Inject

class PostListViewModel() : BaseViewModel() {
    @Inject
    lateinit var postApi: PostApi
    val postListAdapter: PostListAdapter = PostListAdapter()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }

    private var subscription: CompositeDisposable = CompositeDisposable()

    init {
        loadPosts()
    }

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    private fun loadPosts() {
        subscription.add(
            postApi.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                    { result -> onRetrievePostListSuccess(result) },
                    { onRetrievePostListError() }
                )
        )
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(postList: List<Post>) {
        postListAdapter.updatePostList(postList)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.post_error
    }
}