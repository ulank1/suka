package kg.docplus.model.get

import com.quickblox.users.model.QBUser

data class TokenWithVideo(
    val token: String,
    val video_chat_credentials:QBUser
)