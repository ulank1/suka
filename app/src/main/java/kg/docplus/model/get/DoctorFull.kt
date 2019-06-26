package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class DoctorFull (

	@SerializedName("id") val id : Int,
	@SerializedName("doctor_detail") val doctor_detail : DoctorDetail,
	@SerializedName("phone_number") val phone_number : String,
	@SerializedName("services") val services : ArrayList<Services>
)