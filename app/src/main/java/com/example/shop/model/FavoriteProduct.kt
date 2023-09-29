package com.example.shop.model


import androidx.room.Entity

@Entity(tableName = "favorite_products")
data class FavoriteProduct( val id: Long = 0,
                            val productId: String)