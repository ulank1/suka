package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class Doctor_detail (

	@SerializedName("first_name") val first_name : String,
	@SerializedName("mid_name") val mid_name : String,
	@SerializedName("avatar") val avatar : String,
	@SerializedName("specialties") val specialties : List<Specialties>
)