package com.example.shop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val user_id: String= "",
    val product_id: String = "",
    val title:String = "",
    val price: String = "",
    val image: String = "",
    var cart_quantity :String = "1",
    var id: String = ""): Parcelable