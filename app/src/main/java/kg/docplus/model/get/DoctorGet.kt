package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class DoctorGet (

	@SerializedName("id") val id : Int,
	@SerializedName("doctor_detail") val doctor_detail : Doctor_detail,
	@SerializedName("min_price") val min_price : Int
)