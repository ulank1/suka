package kg.docplus.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import kg.docplus.ui.main.MainViewModel
import kg.docplus.ui.main.settings.SettingsViewModel
import kg.docplus.ui.my_doctor.DoctorViewModel
import kg.docplus.ui.notification.NotificationViewModel
import kg.docplus.ui.rating.PreviewModelView

class ViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

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
        if (modelClass.isAssignableFrom(PhoneViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhoneViewModel() as T
        }
        if (modelClass.isAssignableFrom(NewPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewPasswordViewModel() as T
        }
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel() as T
        }
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel() as T
        }
        if (modelClass.isAssignableFrom(DoctorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DoctorViewModel() as T
        }

        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel() as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel() as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        if (modelClass.isAssignableFrom(PreviewModelView::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreviewModelView() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}