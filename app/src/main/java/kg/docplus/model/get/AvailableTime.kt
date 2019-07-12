package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class AvailableTime (

	@SerializedName("free_time") val free_time : ArrayList<String>
)