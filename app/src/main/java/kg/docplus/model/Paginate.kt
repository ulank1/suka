package kg.docplus.model


import java.util.ArrayList

data class Paginate<D>(
        var count:Int = 0,
        var next:Int?=null,
        var results:ArrayList<D> = ArrayList()
)
