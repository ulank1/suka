package kg.docplus.model
import com.google.gson.annotations.SerializedName
import kg.docplus.model.get.PatientDetail2

data class Patient (

		@SerializedName("id") val id : Int,
		@SerializedName("phone_number") val phone_number : String,
		@SerializedName("patient_detail") val patient_detail : PatientDetail2

)