package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val selectedColorName: String,
    val selectedColorHex: Long,
    val selectedSize: String,
    val quantity: Int
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "shipping_addresses")
data class ShippingAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val fullName: String,
    val street: String,
    val cityStateZip: String,
    val phone: String,
    val isDefault: Boolean = false
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val createdAt: Long,
    val statusName: String,
    val deliveryMethodName: String,
    val paymentMethodName: String,
    val recipientName: String,
    val shippingAddressSummary: String,
    val itemsJson: String,
    val subtotal: Double,
    val shippingFee: Double,
    val discountAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double
)
