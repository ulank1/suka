package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class DoctorDetail (

	@SerializedName("avatar") val avatar : String,
	@SerializedName("certificates") val certificates : List<String>,
	@SerializedName("specialties") val specialties : List<String>,
	@SerializedName("patients") val patients : Int,
	@SerializedName("is_favorite") val is_favorite : Boolean,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("mid_name") val mid_name : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("gender") val gender : Int,
	@SerializedName("birth_date") val birth_date : String,
	@SerializedName("extra_phone_number") val extra_phone_number : String,
	@SerializedName("experience") val experience : Int,
	@SerializedName("qualification") val qualification : String,
	@SerializedName("workplace") val workplace : String,
	@SerializedName("extra_info") val extra_info : String,
	@SerializedName("passport_images") val passport_images : List<String>
)