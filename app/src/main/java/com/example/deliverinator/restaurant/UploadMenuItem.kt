package com.example.deliverinator.restaurant

class UploadMenuItem {
    var imageUrl: String? = null
    var itemName: String? = null
    var itemDescription: String? = null
    var isAvailable = false

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
