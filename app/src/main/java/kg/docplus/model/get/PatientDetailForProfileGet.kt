package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class PatientDetailForProfileGet (

	@SerializedName("avatar") val avatar : Avatar?,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("mid_name") val mid_name : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("gender") var gender : String?,
	@SerializedName("birth_date") val birth_date : String?
)

data class Avatar(
		@SerializedName("file") val file : String?
)