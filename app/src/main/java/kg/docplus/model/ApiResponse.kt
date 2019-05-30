package kg.docplus.model


import java.util.ArrayList

class ApiResponse<D> {
    val message: String? = null
    var results: ArrayList<D>? = null

    var error: String? = null
    val isSuccess: Boolean = false
    val status: Boolean = false

}
