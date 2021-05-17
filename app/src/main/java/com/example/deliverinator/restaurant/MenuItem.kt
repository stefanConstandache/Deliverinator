package com.example.deliverinator.restaurant

import android.net.Uri

data class MenuItem(val imageUri: Uri, val itemName: String, val itemDescription: String, val isAvailable: Boolean)
