package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class Specialties (

	@SerializedName("id") val id : Int,
	@SerializedName("title") val title : String
)