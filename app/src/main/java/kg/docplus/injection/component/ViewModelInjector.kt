package kg.docplus.injection.component

import dagger.Component
import kg.docplus.injection.module.NetworkModule
import kg.docplus.post.PostListViewModel
import kg.docplus.post.PostViewModel
import kg.docplus.product.ProductListViewModel
import kg.docplus.product.ProductViewModel
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
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */
    fun inject(postListViewModel: PostListViewModel)
    fun inject(productListViewModel: ProductListViewModel)
    /**
     * Injects required dependencies into the specified PostViewModel.
     * @param postViewModel PostViewModel in which to inject the dependencies
     */
    fun inject(postViewModel: PostViewModel)
    fun inject(productViewModel: ProductViewModel)

    fun inject(loginViewModel: LoginViewModel)
    fun inject(registerViewModel: RegisterViewModel)
    fun inject(confirmCodeViewModel: ConfirmCodeViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(filterViewModel: FilterViewModel)
    fun inject(profileViewMode: ProfileViewModel)
    fun inject(doctorDetailViewModel: DoctorDetailViewModel)
    fun inject(phoneViewModel: PhoneViewModel)
    fun inject(newPasswordViewModel: NewPasswordViewModel)
    fun inject(favouriteViewModel: FavouriteViewModel)
    fun inject(chatViewModel: ChatViewModel)
    fun inject(doctorViewModel: DoctorViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}