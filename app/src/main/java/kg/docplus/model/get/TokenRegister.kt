package kg.docplus.model.get

data class TokenRegister(
    val token: String?,
    val error: String?,
    val is_profile_filled: Boolean?,
    var success:Boolean? = true,
    val message:String

)