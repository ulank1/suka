package kg.docplus.injection.component

import dagger.Component
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

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}