package kg.docplus.model.get.my_doctor
import com.google.gson.annotations.SerializedName

data class Doctor_detail (


	@SerializedName("id") val id : Int,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("mid_name") val mid_name : String,
	@SerializedName("rate") val rate : String,
	@SerializedName("specialties") val specialties : List<Specialties>,
	@SerializedName("avatar") val avatar : String
)