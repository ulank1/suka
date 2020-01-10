package kg.docplus.model.firebase

import com.google.gson.annotations.SerializedName

data class Message (

        @SerializedName("time") val time : String,
        @SerializedName("message") var message : String,
        @SerializedName("user") val user : Int
)