package kg.docplus.ui.favorite_doctor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.databinding.ActivityFavouriteBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.utils.UserToken

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var viewModel: FavouriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DocPlusApp.activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_favourite)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(FavouriteViewModel::class.java)

        binding.viewModel = viewModel

        setupRv()
        viewModel.getDoctorFavourite()


    }

    fun setupRv(){
        var manager = GridLayoutManager(this,2)
        binding.rv.layoutManager = manager
        binding.rv.setHasFixedSize(false)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode",requestCode.toString())
    }

}
