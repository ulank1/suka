package kg.docplus.model.get
import com.google.gson.annotations.SerializedName

data class Days (

		@SerializedName("0") val day0 : Boolean,
		@SerializedName("1") val day1 : Boolean,
		@SerializedName("2") val day2 : Boolean,
		@SerializedName("3") val day3 : Boolean,
		@SerializedName("4") val day4 : Boolean,
		@SerializedName("5") val day5 : Boolean,
		@SerializedName("6") val day6 : Boolean

)