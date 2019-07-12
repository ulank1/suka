package kg.docplus.model.get.my_doctor
import com.google.gson.annotations.SerializedName

data class Appointment (

	@SerializedName("date") val date : String,
	@SerializedName("exact_time") val exact_time : String,
	@SerializedName("service") val service : Int,
	@SerializedName("price") val price : Int
)