package kg.docplus.model.get.my_doctor
import com.google.gson.annotations.SerializedName


data class VideoChatCredentials (

	@SerializedName("tags") val tags : List<String>,
	@SerializedName("password") val password : String,
	@SerializedName("id") val id : Int,
	@SerializedName("full_name") val full_name : String,
	@SerializedName("login") val login : String
)