package kg.docplus.model

import java.util.ArrayList

class Product {

    var id: Int = 0
    var created_at: String? = null
    var new_price: Int = 0
    var updated_at: String? = null
    var title: String? = null
    var description: String? = null
    var price: Int = 0
    var discount: Int = 0
    var gallery: ArrayList<String>? = null
    var gallery_original: ArrayList<String>? = null
    var slug: String? = null

    var isAdult: Boolean = false
    var rating: Int = 0
    var comments_count: Int = 0
    var isIs_favorite: Boolean = false
    var isIs_liked: Boolean = false
    var isIs_owner: Boolean = false
    var isArchived: Boolean = false
    var isHidden: Boolean = false
    var cart_quantity: Int = 0
    val youtube_ids: ArrayList<String>? = null
    var subcategory: Int = 0
    var own_subcategories: List<Int>? = null
    var isIs_user_adult: Boolean = false

}
