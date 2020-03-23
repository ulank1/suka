package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class DropDown (
	@SerializedName("count") val count : Int,
	@SerializedName("next") val next : String,
	@SerializedName("previous") val previous : String,
	@SerializedName("results") val results : ArrayList<Specialties>
)