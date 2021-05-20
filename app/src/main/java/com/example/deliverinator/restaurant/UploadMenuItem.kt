package com.example.deliverinator.restaurant

import com.google.firebase.database.Exclude

class UploadMenuItem {
    var imageUrl: String? = null
    var itemName: String? = null
    var itemDescription: String? = null
    var isAvailable = false
    @get:Exclude
    @set:Exclude
    var key: String? = null

    constructor() {
        // Empty constructor
    }

    constructor(
        imageUrl: String?,
        itemName: String?,
        itemDescription: String?,
        isAvailable: Boolean
    ) {
        this.imageUrl = imageUrl
        this.itemName = itemName
        this.itemDescription = itemDescription
        this.isAvailable = isAvailable
    }
}
