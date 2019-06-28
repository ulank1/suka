package kg.docplus.ui.favorite_doctor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kg.docplus.App
import kg.docplus.R
import kg.docplus.databinding.ActivityFavouriteBinding
import kg.docplus.injection.ViewModelFactory

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var viewModel: FavouriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.activity = this

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
