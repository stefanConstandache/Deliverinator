package com.example.deliverinator.restaurant

import com.google.firebase.database.Exclude

class OrdersItem {
    var clientName: String? = null
    var address: String? = null
    var order: String? = null
    var price: Double = 0.0
    @get:Exclude
    @set:Exclude
    var key: String? = null

    constructor() {
        // Empty constructor
    }

    constructor(
        clientName: String?,
        address: String?,
        order: String?,
        price: Double
    ) {
        this.clientName = clientName
        this.address = address
        this.order = order
        this.price = price
    }
}