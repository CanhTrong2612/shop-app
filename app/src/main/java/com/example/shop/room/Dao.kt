package com.example.shop.room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.shop.model.FavoriteProduct

@Dao
    interface FavoriteProductDao {
        @Query("SELECT * FROM favorite_products")
        fun getAllFavoriteProducts(): LiveData<List<FavoriteProduct>>

        @Insert
        suspend fun insertFavoriteProduct(product: FavoriteProduct)

        @Delete
        suspend fun deleteFavoriteProduct(product: FavoriteProduct)
    }
