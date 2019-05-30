package kg.docplus.base

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import kg.docplus.DocPlusApp
import kg.docplus.injection.component.DaggerViewModelInjector
import kg.docplus.injection.component.ViewModelInjector
import kg.docplus.injection.module.NetworkModule
import kg.docplus.post.PostListViewModel
import kg.docplus.post.PostViewModel
import kg.docplus.product.ProductListViewModel
import kg.docplus.product.ProductViewModel
import kg.docplus.ui.doctor_deatail.DoctorDetailViewModel
import kg.docplus.ui.login.LoginViewModel
import kg.docplus.ui.main.home.HomeViewModel
import kg.docplus.ui.main.search.FilterViewModel
import kg.docplus.ui.profile.ProfileViewModel
import kg.docplus.ui.register.RegisterViewModel
import kg.docplus.ui.register.confirm_code.ConfirmCodeViewModel

abstract class BaseViewModel():ViewModel(){

    private var progressBar: ProgressDialog? = null



     fun showProgress() {
        if (progressBar!=null){
            progressBar!!.dismiss()
            progressBar = null
        }
        progressBar = ProgressDialog(DocPlusApp.activity)
        progressBar!!.show()
    }

     fun hideProgress() {
        Log.e("DDD","ddsas");
        if (progressBar != null /*&& progressBar!!.isShowing*/) {
            progressBar!!.dismiss()
        }
        progressBar = null
    }

    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
            is PostViewModel -> injector.inject(this)

            is ProductListViewModel -> injector.inject(this)
            is ProductViewModel -> injector.inject(this)

            is LoginViewModel -> injector.inject(this)
            is RegisterViewModel -> injector.inject(this)
            is ConfirmCodeViewModel -> injector.inject(this)
            is HomeViewModel -> injector.inject(this)
            is FilterViewModel -> injector.inject(this)
            is ProfileViewModel -> injector.inject(this)
            is DoctorDetailViewModel -> injector.inject(this)

        }
    }
}