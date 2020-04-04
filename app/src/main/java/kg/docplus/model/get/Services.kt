package kg.docplus.model.get

import com.google.gson.annotations.SerializedName

data class Services (

	@SerializedName("min_price") val min_price : Int,
	@SerializedName("service") val service : Int
)