package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class Cities (

		@SerializedName("id") val id : Int,
		@SerializedName("name") val name : String

)