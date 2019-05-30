package kg.docplus.product

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kg.docplus.R
import kg.docplus.databinding.ActivityProductLastBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.utils.EndlessRecyclerOnScrollListener

class ProductLastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductLastBinding
    private lateinit var viewModel: ProductListViewModel
    private var errorSnackbar: Snackbar? = null
    private var listener: EndlessRecyclerOnScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_last)
        var linearLayoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.postList.layoutManager = linearLayoutManager
        listener = object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
            override fun onLoadMore(current_page: Int) {
                load(current_page)
            }
        }
        binding.postList.addOnScrollListener(listener as EndlessRecyclerOnScrollListener)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(ProductListViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
                errorMessage -> if(errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
        load(1)
    }

    private fun load(current_page: Int) {
        viewModel.loadPosts(current_page)
    }


    private fun showError(@StringRes errorMessage:Int){
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError(){
        errorSnackbar?.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("dddd",requestCode.toString())
    }

}
