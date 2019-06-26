package kg.docplus.model.post
import com.google.gson.annotations.SerializedName
import kg.docplus.model.get.PatientDetail

data class ProfilePost (

	@SerializedName("patient_detail") val patient_detail : PatientDetail
)