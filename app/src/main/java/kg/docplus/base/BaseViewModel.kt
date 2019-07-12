package kg.docplus.base

import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.ViewModel
import kg.docplus.App
import kg.docplus.injection.component.DaggerViewModelInjector
import kg.docplus.injection.component.ViewModelInjector
import kg.docplus.injection.module.NetworkModule
import kg.docplus.ui.auth.change_password.PhoneViewModel
import kg.docplus.ui.auth.change_password.new_password.NewPasswordViewModel
import kg.docplus.ui.doctor_deatail.DoctorDetailViewModel
import kg.docplus.ui.auth.login.LoginViewModel
import kg.docplus.ui.main.home.HomeViewModel
import kg.docplus.ui.main.search.FilterViewModel
import kg.docplus.ui.profile.ProfileViewModel
import kg.docplus.ui.auth.register.RegisterViewModel
import kg.docplus.ui.auth.register.confirm_code.ConfirmCodeViewModel
import kg.docplus.ui.chat.ChatViewModel
import kg.docplus.ui.favorite_doctor.FavouriteViewModel
import kg.docplus.ui.my_doctor.DoctorViewModel

abstract class BaseViewModel(): ViewModel(){

    private var progressBar: ProgressDialog? = null



     fun showProgress() {
        if (progressBar!=null){
            progressBar!!.dismiss()
            progressBar = null
        }
        progressBar = ProgressDialog(App.activity)
        progressBar!!.show()
    }

     fun hideProgress() {
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

            is LoginViewModel -> injector.inject(this)
            is RegisterViewModel -> injector.inject(this)
            is ConfirmCodeViewModel -> injector.inject(this)
            is HomeViewModel -> injector.inject(this)
            is FilterViewModel -> injector.inject(this)
            is ProfileViewModel -> injector.inject(this)
            is DoctorDetailViewModel -> injector.inject(this)
            is PhoneViewModel -> injector.inject(this)
            is NewPasswordViewModel -> injector.inject(this)
            is FavouriteViewModel -> injector.inject(this)
            is ChatViewModel -> injector.inject(this)
            is DoctorViewModel -> injector.inject(this)

        }
    }
}