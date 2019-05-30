package kg.docplus.model.get

data class TokenRegister(
    val token: String?,
    val error: String?,
    val is_profile_fulled: Boolean?,
    var success:Boolean? = true
)