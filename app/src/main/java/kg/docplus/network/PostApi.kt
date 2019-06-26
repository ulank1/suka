package kg.docplus.network

import android.arch.persistence.room.Delete
import io.reactivex.Observable
import io.reactivex.Single
import kg.docplus.model.Token
import kg.docplus.model.ApiResponse
import kg.docplus.model.Product
import kg.docplus.model.Post
import kg.docplus.model.get.*
import kg.docplus.model.post.ProfilePost
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface PostApi {
    /**
     * Get the list of the pots from the API
     */
    @GET("/posts")
    fun getPosts(): Observable<List<Post>>

    @GET("index/products/last")
    fun getProducts(@Query("category_id") category_id: String, @Query("page") page: Int): Observable<Response<ApiResponse<Product>>>

    @FormUrlEncoded
    @POST("doc-plus/sign-in")
    fun login(
        @Field("phone_number") phone: String,
        @Field("password") password: String
    ): Observable<Response<Token>>

    @FormUrlEncoded
    @POST("doc-plus/sign-up")
    fun register(
        @Field("phone_number") email: String,
        @Field("password") password: String
    ): Observable<Response<Token>>

    @FormUrlEncoded
    @POST("doc-plus/change-password")
    fun newPassword(
        @Field("new_password") password: String,
        @Field("phone_number") phone: String
    ): Observable<Response<Any>>

    @GET("doc-plus/user-exists/")
    fun isUserExist(@Query("phone_number") phone:String): Observable<Response<TokenRegister>>

    @GET("doc-plus/doctors")
    fun getDocs(
        @Query("min_price") min_price:Int,
        @Query("max_price") max_price:Int,
        @Query("service") service:ArrayList<Int>,
        @Query("schedule_time") firstTime:String,
        @Query("schedule_time") secondTime:String,
        @Query("name") name:String,
        @Query("specialty_title") specialty_title:String,
        @Query("schedule_date") date:String,
        @Query("ordering") ordering:String?
    ): Observable<Response<List<DoctorGet>>>

    @GET("doc-plus/doctors")
    fun getDocsTest(): Observable<Response<List<DoctorGet>>>


    @GET("doc-plus/dropdown-list")
    fun getDropdown(@Query("page") page: String): Observable<Response<DropDown>>

    @GET("doc-plus/doctor-page/{id}")
    fun getDocFull(@Path("id") id: Int): Observable<Response<DoctorFull>>

    @GET("doc-plus/favorite-doctor/")
    fun getDocFavourite(): Observable<Response<ArrayList<DoctorGet>>>

    @GET("doc-plus/profile")
    fun getProfile(): Observable<Response<ProfileGet>>

    @GET("doc-plus/dropdown-filter")
    fun getDropDownFilter(
        @Query("page") page: String,
        @Query("q") q:String)
            : Observable<Response<DropDown>>

    @DELETE("doc-plus/favorite-doctor/{id}/")
    fun deleteFavorite(
       @Path("id") id: Int
    ): Observable<Response<String>>

    @PUT("doc-plus/favorite-doctor/{id}/")
    fun createFavorite(
        @Path("id") id: Int
    ): Observable<Response<Any>>
    @Multipart
    @POST("core/media")
    fun postImage(@Part file: MultipartBody.Part): Observable<Response<UrlImage>>

    @PUT("doc-plus/profile")
    fun putProfile(
        @Body patientDetail: ProfilePost
    ): Observable<Response<Any>>

}