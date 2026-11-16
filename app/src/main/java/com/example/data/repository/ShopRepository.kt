package com.example.data.repository

import com.example.data.local.CatalogDataSource
import com.example.data.local.OrderEntity
import com.example.data.local.ShippingAddressEntity
import com.example.data.local.ShopDao
import com.example.data.local.CartItemEntity
import com.example.data.local.WishlistItemEntity
import com.example.data.model.BagPricingSummary
import com.example.data.model.CartItemWithProduct
import com.example.data.model.DeliveryMethod
import com.example.data.model.OrderLineItem
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.data.model.PromoEvaluation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.math.max
import kotlin.math.roundToInt

class ShopRepository(private val shopDao: ShopDao) {

    private val json = Json { ignoreUnknownKeys = true }

    val catalog: List<Product> = CatalogDataSource.products

    val cartItems: Flow<List<CartItemWithProduct>> = shopDao.observeCartItems().map { entities ->
        entities.mapNotNull { entity ->
            val product = CatalogDataSource.findById(entity.productId) ?: return@mapNotNull null
            CartItemWithProduct(
                cartItemId = entity.id,
                product = product,
                selectedColorName = entity.selectedColorName,
                selectedColorHex = entity.selectedColorHex,
                selectedSize = entity.selectedSize,
                quantity = entity.quantity
            )
        }
    }

    val wishlistIds: Flow<Set<String>> = shopDao.observeWishlistItems().map { list ->
        list.map { it.productId }.toSet()
    }

    val addresses: Flow<List<ShippingAddressEntity>> = shopDao.observeAddresses()

    val orders: Flow<List<OrderEntity>> = shopDao.observeOrders()

    suspend fun ensureSeededDefaults() {
        if (shopDao.addressCount() == 0) {
            shopDao.insertAddress(
                ShippingAddressEntity(
                    label = "Studio Home",
                    fullName = "Alex Mercer",
                    street = "742 Evergreen Terrace, Apt 4B",
                    cityStateZip = "Portland, OR 97205",
                    phone = "(503) 555-0194",
                    isDefault = true
                )
            )
        }
        if (shopDao.orderCount() == 0) {
            val sampleItems = listOf(
                OrderLineItem(
                    productId = "kyoto-pour-over",
                    productName = "Kyoto Stoneware Pour-Over",
                    colorName = "Chalk",
                    colorHex = 0xFFEBE4DA,
                    size = "500 ml Set",
                    unitPrice = 78.0,
                    quantity = 1
                ),
                OrderLineItem(
                    productId = "machined-brass-pen",
                    productName = "Hex Machined Brass Pen",
                    colorName = "Raw Brass",
                    colorHex = 0xFFCBA052,
                    size = "0.5 mm Fine",
                    unitPrice = 58.0,
                    quantity = 1
                )
            )
            shopDao.insertOrder(
                OrderEntity(
                    orderId = "KNS-84192",
                    createdAt = System.currentTimeMillis() - 4L * 24 * 60 * 60 * 1000,
                    statusName = OrderStatus.SHIPPED.name,
                    deliveryMethodName = DeliveryMethod.STANDARD.name,
                    paymentMethodName = PaymentMethod.CARD_VISA.name,
                    recipientName = "Alex Mercer",
                    shippingAddressSummary = "742 Evergreen Terrace, Apt 4B, Portland, OR 97205",
                    itemsJson = json.encodeToString(sampleItems),
                    subtotal = 136.0,
                    shippingFee = 12.0,
                    discountAmount = 0.0,
                    taxAmount = 10.88,
                    totalAmount = 158.88
                )
            )
        }
    }

    suspend fun addToBag(
        product: Product,
        colorName: String,
        colorHex: Long,
        size: String,
        quantityDelta: Int = 1
    ) {
        val existing = shopDao.findCartItem(product.id, colorName, size)
        if (existing != null) {
            shopDao.updateCartQuantity(existing.id, existing.quantity + quantityDelta)
        } else {
            shopDao.upsertCartItem(
                CartItemEntity(
                    productId = product.id,
                    selectedColorName = colorName,
                    selectedColorHex = colorHex,
                    selectedSize = size,
                    quantity = quantityDelta
                )
            )
        }
    }

    suspend fun updateQuantity(cartItemId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            shopDao.deleteCartItem(cartItemId)
        } else {
            shopDao.updateCartQuantity(cartItemId, newQuantity)
        }
    }

    suspend fun removeFromBag(cartItemId: Long) {
        shopDao.deleteCartItem(cartItemId)
    }

    suspend fun toggleWishlist(productId: String, currentlySaved: Boolean) {
        if (currentlySaved) {
            shopDao.deleteWishlistItem(productId)
        } else {
            shopDao.insertWishlistItem(WishlistItemEntity(productId = productId))
        }
    }

    suspend fun addAddress(
        label: String,
        fullName: String,
        street: String,
        cityStateZip: String,
        phone: String,
        makeDefault: Boolean
    ) {
        if (makeDefault) {
            shopDao.clearDefaultAddress()
        }
        val id = shopDao.insertAddress(
            ShippingAddressEntity(
                label = label.ifBlank { "Address" },
                fullName = fullName,
                street = street,
                cityStateZip = cityStateZip,
                phone = phone,
                isDefault = makeDefault
            )
        )
        if (makeDefault) {
            shopDao.setDefaultAddress(id)
        }
    }

    suspend fun setDefaultAddress(addressId: Long) {
        shopDao.clearDefaultAddress()
        shopDao.setDefaultAddress(addressId)
    }

    suspend fun deleteAddress(addressId: Long) {
        shopDao.deleteAddress(addressId)
    }

    fun evaluatePromo(codeRaw: String?, subtotal: Double): PromoEvaluation? {
        val normalized = codeRaw?.trim()?.uppercase()?.takeIf { it.isNotEmpty() } ?: return null
        return when (normalized) {
            "KANSO15" -> PromoEvaluation(
                code = "KANSO15",
                isValid = true,
                discountAmount = roundCents(subtotal * 0.15),
                waivesShipping = false,
                message = "KANSO15 applied: 15% off your order"
            )
            "WELCOME10" -> PromoEvaluation(
                code = "WELCOME10",
                isValid = true,
                discountAmount = roundCents(subtotal * 0.10),
                waivesShipping = false,
                message = "WELCOME10 applied: 10% off your order"
            )
            "FREESHIP" -> PromoEvaluation(
                code = "FREESHIP",
                isValid = true,
                discountAmount = 0.0,
                waivesShipping = true,
                message = "FREESHIP applied: Complimentary shipping"
            )
            else -> PromoEvaluation(
                code = normalized,
                isValid = false,
                discountAmount = 0.0,
                waivesShipping = false,
                message = "Code \"$normalized\" not recognized. Try KANSO15 or FREESHIP."
            )
        }
    }

    fun calculatePricing(
        items: List<CartItemWithProduct>,
        promoCode: String?,
        deliveryMethod: DeliveryMethod = DeliveryMethod.STANDARD
    ): BagPricingSummary {
        val subtotal = roundCents(items.sumOf { it.lineTotal })
        val promo = evaluatePromo(promoCode, subtotal)
        val discount = if (promo?.isValid == true) promo.discountAmount else 0.0
        val freeShippingThreshold = 150.0
        val baseShipping = when {
            items.isEmpty() -> 0.0
            deliveryMethod == DeliveryMethod.STUDIO_PICKUP -> 0.0
            deliveryMethod == DeliveryMethod.EXPRESS -> deliveryMethod.fee
            promo?.isValid == true && promo.waivesShipping -> 0.0
            subtotal >= freeShippingThreshold -> 0.0
            else -> DeliveryMethod.STANDARD.fee
        }
        val taxableAmount = max(0.0, subtotal - discount)
        val tax = roundCents(taxableAmount * 0.08)
        val total = roundCents(taxableAmount + baseShipping + tax)
        val remainingForFree = max(0.0, roundCents(freeShippingThreshold - subtotal))

        return BagPricingSummary(
            subtotal = subtotal,
            shippingFee = baseShipping,
            discountAmount = discount,
            estimatedTax = tax,
            total = total,
            amountForFreeShipping = remainingForFree,
            promoMessage = promo?.message,
            appliedPromoCode = if (promo?.isValid == true) promo.code else null
        )
    }

    suspend fun placeOrder(
        items: List<CartItemWithProduct>,
        address: ShippingAddressEntity,
        deliveryMethod: DeliveryMethod,
        paymentMethod: PaymentMethod,
        pricing: BagPricingSummary
    ): String {
        val orderNumber = "KNS-${(10000..99999).random()}"
        val lineItems = items.map {
            OrderLineItem(
                productId = it.product.id,
                productName = it.product.name,
                colorName = it.selectedColorName,
                colorHex = it.selectedColorHex,
                size = it.selectedSize,
                unitPrice = it.product.price,
                quantity = it.quantity
            )
        }
        val order = OrderEntity(
            orderId = orderNumber,
            createdAt = System.currentTimeMillis(),
            statusName = OrderStatus.CONFIRMED.name,
            deliveryMethodName = deliveryMethod.name,
            paymentMethodName = paymentMethod.name,
            recipientName = address.fullName,
            shippingAddressSummary = "${address.street}, ${address.cityStateZip}",
            itemsJson = json.encodeToString(lineItems),
            subtotal = pricing.subtotal,
            shippingFee = pricing.shippingFee,
            discountAmount = pricing.discountAmount,
            taxAmount = pricing.estimatedTax,
            totalAmount = pricing.total
        )
        shopDao.insertOrder(order)
        shopDao.clearCart()
        return orderNumber
    }

    fun decodeOrderItems(itemsJson: String): List<OrderLineItem> {
        return try {
            json.decodeFromString<List<OrderLineItem>>(itemsJson)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun roundCents(value: Double): Double = (value * 100.0).roundToInt() / 100.0
}
