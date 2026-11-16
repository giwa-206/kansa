package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object DiscoverRoute

@Serializable
object ExploreRoute

@Serializable
object SavedRoute

@Serializable
object BagRoute

@Serializable
object ProfileRoute

@Serializable
data class ProductDetailRoute(val productId: String)

@Serializable
object CheckoutRoute

@Serializable
data class OrderSuccessRoute(val orderId: String)
