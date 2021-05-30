package com.example.deliverinator.admin

class DeleteFragmentRecyclerItem {
    //val imageResource: Int,
    var imageUrl: String? = null
    var text1: String? = null
    var text2: String? = null

    constructor() {
        // Empty constructor
    }

    constructor(
        imageUrl: String?,
        text1: String?,
        text2: String?,
    ) {
        this.imageUrl = imageUrl
        this.text1 = text1
        this.text2 = text2
    }
}