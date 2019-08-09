package kg.docplus.utils

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kg.docplus.R
import kg.docplus.model.get.Services
import kg.docplus.model.get.Specialties
import kg.docplus.utils.extension.getParentActivity
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kg.docplus.utils.extension.setRoundedImage


@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("mutableError")
fun setMutableError(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.error = value ?: "" })
    }
}

@BindingAdapter("mutableArray")
fun setMutableTextArray(view: TextView, texts: List<Specialties>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && texts != null) {
        for ((i, text) in texts.withIndex()) {
            if (i == texts.size - 1) {
                view.append(text.title)
            } else {
                view.append("${text.title}, ")
            }
        }

    }
}

@BindingAdapter("mutableStatus")
fun setMutableStatus(view: ImageView, isActive: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && isActive != null) {
        isActive.observe(
            parentActivity,
            Observer { value ->
                if (value!!) view.setImageResource(kg.docplus.R.drawable.heart_activated) else view.setImageResource(kg.docplus.R.drawable.heart_disactivated)
            })
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("mutableImageUrl")
fun setImage(view: ImageView, url: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && url != null) {
        val sCorner = 15
        val sMargin = 20

        val sBorder = 10
        val sColor = "#7D9067"
        url.observe(parentActivity, Observer {
            Glide.with(parentActivity).load(it)
                .apply(
                    RequestOptions.bitmapTransform(
                        (CropCircleTransformation())
                    )
                ).into(view)
        })

    }
}


@BindingAdapter("mutableRoundedImageUrl")
fun setRoundedImage(view: ImageView, url: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && url != null) {
        val sCorner = 15
        val sMargin = 20

        val sBorder = 10
        val sColor = "#7D9067"
        url.observe(parentActivity, Observer {
           setRoundedImage(view,it,parentActivity)
        })

    }
}


@BindingAdapter("mutableLinearVisibility")
fun setMutableLinearVisibility(view: View, services: ArrayList<Services>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && services != null) {
        view.gone()
        var id = view.id
        var status = -1

        when (id) {
            R.id.chat -> status = 1
            R.id.video_chat -> status = 2
            R.id.appointment -> status = 0
            R.id.call_home -> status = 3
        }

        for (service in services) {
            if (service.service == status) {
                view.visible()
            }
        }
    }
}

@BindingAdapter("mutableTextM")
fun setMutableTextM(view: TextView, services: ArrayList<Services>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && services != null) {

        var id = view.id
        var status = -1

        when (id) {
            R.id.price_chat -> status = 1
            R.id.price_video_chat -> status = 2
            R.id.price_appointment -> status = 0
            R.id.price_call_home -> status = 3
        }

        for (service in services) {
            if (service.service == status) {
                view.text = "${service.min_price} сом"
            }
        }
    }
}


@BindingAdapter("mutableGridManager")
fun setMutableGridManager(view: RecyclerView, text: String) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null) {
        var manager = GridLayoutManager(parentActivity, 2)
        view.layoutManager = manager
        view.setHasFixedSize(false)
    }
}

@BindingAdapter("textName")
fun setTextName(view: TextView, text: String) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null) {
        if (text.length<6|| text.contains("null")){
            view.text = ""
        }else{
            view.text = text
        }
    }
}