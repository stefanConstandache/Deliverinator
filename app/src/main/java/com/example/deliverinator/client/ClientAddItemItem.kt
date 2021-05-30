package com.example.deliverinator.client

class ClientAddItemItem {

    var imageUrl: String? = null
    var name: String? = null
    var description: String? = null

    constructor() {
        // Empty constructor
    }

    constructor(
        imageUrl: String?,
        name: String?,
        description: String?,
    ) {
        this.imageUrl = imageUrl
        this.name = name
        this.description = description
    }
}