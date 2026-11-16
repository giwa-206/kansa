package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    // Cart
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Query(
        "SELECT * FROM cart_items WHERE productId = :productId AND selectedColorName = :colorName AND selectedSize = :size LIMIT 1"
    )
    suspend fun findCartItem(productId: String, colorName: String, size: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCartItem(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartQuantity(id: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Long)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Wishlist
    @Query("SELECT * FROM wishlist_items ORDER BY addedAt DESC")
    fun observeWishlistItems(): Flow<List<WishlistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteWishlistItem(productId: String)

    // Shipping Addresses
    @Query("SELECT * FROM shipping_addresses ORDER BY isDefault DESC, id ASC")
    fun observeAddresses(): Flow<List<ShippingAddressEntity>>

    @Query("SELECT COUNT(*) FROM shipping_addresses")
    suspend fun addressCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: ShippingAddressEntity): Long

    @Query("UPDATE shipping_addresses SET isDefault = 0")
    suspend fun clearDefaultAddress()

    @Query("UPDATE shipping_addresses SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultAddress(id: Long)

    @Query("DELETE FROM shipping_addresses WHERE id = :id")
    suspend fun deleteAddress(id: Long)

    // Orders
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun observeOrders(): Flow<List<OrderEntity>>

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun orderCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
}
