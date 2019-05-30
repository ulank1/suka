package kg.docplus.post

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Post
import kg.docplus.product.ProductLastActivity

class PostViewModel(val context:Context): BaseViewModel() {
    private val postTitle = MutableLiveData<String>()
    private val postBody = MutableLiveData<String>()

    fun bind(post: Post){
        postTitle.value = post.title
        postBody.value = post.body
    }

    fun getPostTitle():MutableLiveData<String>{
        return postTitle
    }

    fun getPostBody():MutableLiveData<String>{
        return postBody
    }

    fun startActivity(){
        context.startActivity(Intent(context,ProductLastActivity::class.java))
    }
}