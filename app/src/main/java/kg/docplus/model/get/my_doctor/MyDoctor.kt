package kg.docplus.model.get.my_doctor
import com.google.gson.annotations.SerializedName


data class MyDoctor (
	@SerializedName("date") val date : String,
	@SerializedName("exact_time") val exact_time : String,
	@SerializedName("service") val service : Int,
	@SerializedName("patient_id") val patient_id : Int,
	@SerializedName("price") val price : Int,
	@SerializedName("id") val id : Int,
	@SerializedName("doctor_detail") val doctor_detail : Doctor_detail,
	@SerializedName("video_chat_credentials") val video_chat_credentials : VideoChatCredentials
)