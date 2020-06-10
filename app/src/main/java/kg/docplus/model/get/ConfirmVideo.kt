package kg.docplus.model.get
import com.google.gson.annotations.SerializedName
import kg.docplus.model.get.my_doctor.VideoChatCredentials

data class ConfirmVideo (
		@SerializedName("calculated_price") val calculated_price : Int,
		@SerializedName("video_chat_credentials") val video_chat_credentials : VideoChatCredentials

)