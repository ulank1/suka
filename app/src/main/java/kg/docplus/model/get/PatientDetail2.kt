package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class PatientDetail2 (

	@SerializedName("avatar") val avatar : UrlImage?,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("mid_name") val mid_name : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("gender") var gender : String?,
	@SerializedName("birth_date") val birth_date : String?
)