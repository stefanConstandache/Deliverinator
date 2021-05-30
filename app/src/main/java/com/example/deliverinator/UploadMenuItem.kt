package com.example.deliverinator

import com.google.firebase.database.Exclude
import java.io.Serializable

class UploadMenuItem : Serializable {
    var imageUrl: String? = null
    var itemName: String? = null
    var itemDescription: String? = null
    var itemPrice: Double = 0.0
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
        itemPrice: Double,
        isAvailable: Boolean
    ) {
        this.imageUrl = imageUrl
        this.itemName = itemName
        this.itemDescription = itemDescription
        this.itemPrice = itemPrice
        this.isAvailable = isAvailable
    }
}
