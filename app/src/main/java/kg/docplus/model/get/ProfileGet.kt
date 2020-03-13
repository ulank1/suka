package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class ProfileGet (

	@SerializedName("id") val id : Int,
	@SerializedName("patient_detail") val patient_detail : PatientDetailForProfileGet,
	@SerializedName("phone_number") val phone_number : String
)