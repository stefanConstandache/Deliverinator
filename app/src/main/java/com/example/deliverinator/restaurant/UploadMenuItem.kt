package com.example.deliverinator.restaurant

class UploadMenuItem() {
    private var mImageUrl: String = ""
    private var mItemName: String = ""
    private var mItemDescription: String = ""

    val imageUrl
        get() = mImageUrl

    val itemName
        get() = mItemName

    val itemDescription
        get() = mItemDescription

    constructor(imageUrl: String, itemName: String, itemDescription: String) : this() {
        this.mImageUrl = imageUrl
        this.mItemName = itemName
        this.mItemDescription = itemDescription
    }
}
