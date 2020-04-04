package kg.docplus.model.get
import com.google.gson.annotations.SerializedName
import kg.docplus.model.Patient

data class Preview (

		@SerializedName("id") val id : Int,
		@SerializedName("rate") val rate : Int,
		@SerializedName("created_at") val created_at : String,
		@SerializedName("patient") val patient : Patient,
		@SerializedName("comment") val comment : String

)