package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class DoctorGet (

	@SerializedName("id") val id : Int,
	@SerializedName("doctor_detail") val doctor_detail : DoctorDetail,
	@SerializedName("working_days") val working_days : Days?,
	@SerializedName("services") val services : ArrayList<Services>
)