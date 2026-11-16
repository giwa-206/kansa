package com.example.data.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

enum class ProductCategory(val label: String) {
    ALL("All"),
    HOME("Home"),
    TECH("Tech"),
    CARRY("Carry"),
    APPAREL("Apparel")
}

enum class SortOption(val label: String) {
    FEATURED("Featured"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    TOP_RATED("Top Rated")
}

enum class ProductVisualType {
    PHOTO_CERAMICS,
    PHOTO_AUDIO,
    PHOTO_TOTE,
    ILLUSTRATION_LAMP,
    ILLUSTRATION_WATCH,
    ILLUSTRATION_CARAFE,
    ILLUSTRATION_BLANKET,
    ILLUSTRATION_SPEAKER,
    ILLUSTRATION_PEN,
    ILLUSTRATION_DIFFUSER,
    ILLUSTRATION_OVERSHIRT,
    ILLUSTRATION_CARDHOLDER
}

data class ProductColorOption(
    val name: String,
    val hex: Long
)

data class ProductReview(
    val author: String,
    val rating: Int,
    val date: String,
    val comment: String
)

data class Product(
    val id: String,
    val name: String,
    val subtitle: String,
    val category: ProductCategory,
    val price: Double,
    val originalPrice: Double? = null,
    val rating: Double,
    val reviewCount: Int,
    val description: String,
    val materialsAndSpecs: String,
    val shippingInfo: String,
    val badge: String? = null,
    val isFeatured: Boolean = false,
    val visualType: ProductVisualType,
    @DrawableRes val photoResId: Int? = null,
    val colors: List<ProductColorOption>,
    val sizes: List<String>,
    val reviews: List<ProductReview>
) {
    val isOnSale: Boolean get() = originalPrice != null && originalPrice > price
    val discountPercent: Int
        get() = if (originalPrice != null && originalPrice > price) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else 0
}

data class CartItemWithProduct(
    val cartItemId: Long,
    val product: Product,
    val selectedColorName: String,
    val selectedColorHex: Long,
    val selectedSize: String,
    val quantity: Int
) {
    val lineTotal: Double get() = product.price * quantity
}

@Serializable
data class OrderLineItem(
    val productId: String,
    val productName: String,
    val colorName: String,
    val colorHex: Long,
    val size: String,
    val unitPrice: Double,
    val quantity: Int
)

enum class OrderStatus(val label: String, val stepIndex: Int) {
    CONFIRMED("Confirmed", 0),
    PREPARING("Preparing", 1),
    SHIPPED("Shipped", 2),
    DELIVERED("Delivered", 3)
}

enum class DeliveryMethod(
    val title: String,
    val subtitle: String,
    val fee: Double
) {
    STANDARD("Standard Ground", "3–5 business days • Free over $150", 12.0),
    EXPRESS("Express Courier", "1–2 business days • Carbon-neutral", 18.0),
    STUDIO_PICKUP("Studio Pickup", "Ready in 4 hours • Downtown Showroom", 0.0)
}

enum class PaymentMethod(
    val title: String,
    val subtitle: String
) {
    CARD_VISA("Visa •••• 4242", "Expires 09/28 • Default card"),
    GOOGLE_PAY("Digital Wallet", "Express biometric checkout"),
    INSTALLMENTS("4 Interest-Free Payments", "Split every 2 weeks • 0% APR")
}

data class PromoEvaluation(
    val code: String,
    val isValid: Boolean,
    val discountAmount: Double,
    val waivesShipping: Boolean,
    val message: String
)

data class BagPricingSummary(
    val subtotal: Double,
    val shippingFee: Double,
    val discountAmount: Double,
    val estimatedTax: Double,
    val total: Double,
    val amountForFreeShipping: Double,
    val promoMessage: String? = null,
    val appliedPromoCode: String? = null
)
