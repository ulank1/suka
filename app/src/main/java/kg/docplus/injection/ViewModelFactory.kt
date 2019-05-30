package kg.docplus.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kg.docplus.post.PostListViewModel
import kg.docplus.product.ProductListViewModel
import kg.docplus.ui.doctor_deatail.DoctorDetailViewModel
import kg.docplus.ui.login.LoginViewModel
import kg.docplus.ui.main.home.HomeViewModel
import kg.docplus.ui.main.search.FilterViewModel
import kg.docplus.ui.profile.ProfileViewModel
import kg.docplus.ui.register.RegisterViewModel
import kg.docplus.ui.register.confirm_code.ConfirmCodeViewModel

class ViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel() as T
        }
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel() as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel() as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel() as T
        }
        if (modelClass.isAssignableFrom(ConfirmCodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfirmCodeViewModel() as T
        }

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel() as T
        }

        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilterViewModel() as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel() as T
        }
        if (modelClass.isAssignableFrom(DoctorDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DoctorDetailViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}