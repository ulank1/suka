package kg.docplus.product

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.databinding.BindingAdapter
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import kg.docplus.base.BaseViewModel
import kg.docplus.model.Product
import kg.docplus.model.Slider
import kg.docplus.post.PostListActivity
import kg.docplus.utils.extension.getParentActivity
import java.util.*


class ProductViewModel : BaseViewModel() {

    private val postTitle = MutableLiveData<String>()
    private val postDesc = MutableLiveData<String>()
    var product: Product = Product()
    val sliders = ArrayList<Slider>()

    fun bind(post: Product) {
        product = post
        postTitle.value = post.title
        postDesc.value = post.description

        if (product!!.gallery != null) {
            for (image in product!!.gallery!!) {
                val slider = Slider()
                slider.image = image
                sliders.add(slider)
            }

            if (product.youtube_ids != null) {
                for (yId in product.youtube_ids!!) {
                    val slider = Slider()
                    slider.image = "https://i.ytimg.com/vi/$yId/maxresdefault.jpg"
                    slider.isVideo = true
                    slider.youtube_id = yId
                    sliders.add(slider)
                }
            }

//            pageAdapter.setOnClickListener({ pos -> listener.onItemClick(product, position) })
        }
    }

    fun getTitle(): MutableLiveData<String> {
        return postTitle
    }

    fun getDesc(): MutableLiveData<String> {
        return postDesc
    }

    fun getImageUrl(): String {
        if (product.gallery == null) return null.toString()
        if (product.gallery!!.size == 0) return null.toString()
        return product.gallery!![0]
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl)
                .into(view)
        }
    }

    fun performClick(title: String,view: View) {
        Log.d("TAG", "Username: $title ${product.id}")
        val activity:ProductLastActivity = view.getParentActivity() as ProductLastActivity
        activity.startActivityForResult(Intent(activity,PostListActivity::class.java),2)
    }
}