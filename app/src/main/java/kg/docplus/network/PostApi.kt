package kg.docplus.network

import com.quickblox.users.model.QBUser
import io.reactivex.Observable
import io.reactivex.Single
import kg.docplus.model.Token
import kg.docplus.model.ApiResponse
import kg.docplus.model.get.*
import kg.docplus.model.get.my_doctor.MyDoctor
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

    @FormUrlEncoded
    @POST("doc-plus/sign-in")
    fun login(
        @Field("phone_number") phone: String,
        @Field("password") password: String
    ): Observable<Response<TokenWithVideo>>

    @FormUrlEncoded
    @POST("doc-plus/sign-up")
    fun register(
        @Field("phone_number") email: String,
        @Field("password") password: String,
        @Field("video_chat_credentials") qbUser: String
    ): Observable<Response<Token>>

    @FormUrlEncoded
    @POST("doc/device")
    fun postDeviceId(
        @Field("registration_id") registration_id: String,
        @Field("type") type: String
    ): Observable<Response<Any>>


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
        @Query("service") service:Int,
        @Query("starts_at_time") firstTime:String,
        @Query("expires_at_time") secondTime:String,
        @Query("specialty_title") specialty_title:String,
        @Query("name") name:String,
        @Query("date") date:String
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

    @GET("doc-plus/available-times")
    fun getAviableTimes(
        @Query("date") date: String,
        @Query("doctor") doctor: Int,
        @Query("service") q:Int)
            : Observable<Response<AvailableTime>>


    @GET("doc-plus/my-doctors")
    fun getMyDoctors()
            : Observable<Response<ArrayList<MyDoctor>>>

    @FormUrlEncoded
    @POST("doc-plus/appointment-request")
    fun postAppointment(
        @Field("service") service: Int,
        @Field("doctor") doctor: Int,
        @Field("exact_time") exact_time: String,
        @Field("date") date: String

        ): Observable<Response<Any?>>
    @FormUrlEncoded
    @POST("doc-plus/send-push")
    fun sendPush(
        @Field("user_id") user_id:Int,
        @Field("data") data:String
    ): Observable<Response<Any>>


    @GET("doc-plus/notification/")
    fun getNotifications()
            : Observable<Response<ArrayList<Notification>>>
}