package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.OrderEntity
import com.example.data.local.ShippingAddressEntity
import com.example.data.model.BagPricingSummary
import com.example.data.model.CartItemWithProduct
import com.example.data.model.DeliveryMethod
import com.example.data.model.OrderLineItem
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.SortOption
import com.example.data.preferences.ThemePreferences
import com.example.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShopUiState(
    val selectedCategory: ProductCategory = ProductCategory.ALL,
    val searchQuery: String = "",
    val exploreCategory: ProductCategory = ProductCategory.ALL,
    val sortOption: SortOption = SortOption.FEATURED,
    val maxPriceFilter: Float = 400f,
    val onlyOnSale: Boolean = false,
    val appliedPromoCode: String? = null,
    val selectedDeliveryMethod: DeliveryMethod = DeliveryMethod.STANDARD,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CARD_VISA,
    val selectedAddressId: Long? = null,
    val isPlacingOrder: Boolean = false
)

class ShopViewModel(
    private val repository: ShopRepository,
    private val themePreferences: ThemePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState

    val allProducts: List<Product> = repository.catalog

    val isDarkMode: StateFlow<Boolean> = themePreferences.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val cartItems: StateFlow<List<CartItemWithProduct>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistIds: StateFlow<Set<String>> = repository.wishlistIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val addresses: StateFlow<List<ShippingAddressEntity>> = repository.addresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bagPricing: StateFlow<BagPricingSummary> = combine(
        cartItems,
        _uiState
    ) { items, state ->
        repository.calculatePricing(
            items = items,
            promoCode = state.appliedPromoCode,
            deliveryMethod = DeliveryMethod.STANDARD
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BagPricingSummary(0.0, 0.0, 0.0, 0.0, 0.0, 150.0)
    )

    val checkoutPricing: StateFlow<BagPricingSummary> = combine(
        cartItems,
        _uiState
    ) { items, state ->
        repository.calculatePricing(
            items = items,
            promoCode = state.appliedPromoCode,
            deliveryMethod = state.selectedDeliveryMethod
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BagPricingSummary(0.0, 0.0, 0.0, 0.0, 0.0, 150.0)
    )

    val bagItemCount: StateFlow<Int> = cartItems
        .combine(_uiState) { items, _ -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        viewModelScope.launch {
            repository.ensureSeededDefaults()
        }
    }

    fun selectDiscoverCategory(category: ProductCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectExploreCategory(category: ProductCategory) {
        _uiState.update { it.copy(exploreCategory = category) }
    }

    fun selectSortOption(sortOption: SortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
    }

    fun updateMaxPriceFilter(maxPrice: Float) {
        _uiState.update { it.copy(maxPriceFilter = maxPrice) }
    }

    fun toggleOnlyOnSale(onlySale: Boolean) {
        _uiState.update { it.copy(onlyOnSale = onlySale) }
    }

    fun resetExploreFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                exploreCategory = ProductCategory.ALL,
                sortOption = SortOption.FEATURED,
                maxPriceFilter = 400f,
                onlyOnSale = false
            )
        }
    }

    fun getFilteredExploreProducts(state: ShopUiState): List<Product> {
        val query = state.searchQuery.trim().lowercase()
        val filtered = allProducts.filter { product ->
            val matchesCategory = state.exploreCategory == ProductCategory.ALL ||
                product.category == state.exploreCategory
            val matchesPrice = product.price <= state.maxPriceFilter
            val matchesSale = !state.onlyOnSale || product.isOnSale
            val matchesQuery = query.isEmpty() ||
                product.name.lowercase().contains(query) ||
                product.subtitle.lowercase().contains(query) ||
                product.category.label.lowercase().contains(query) ||
                product.materialsAndSpecs.lowercase().contains(query)
            matchesCategory && matchesPrice && matchesSale && matchesQuery
        }
        return when (state.sortOption) {
            SortOption.FEATURED -> filtered.sortedByDescending { it.isFeatured }
            SortOption.PRICE_LOW_HIGH -> filtered.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.price }
            SortOption.TOP_RATED -> filtered.sortedByDescending { it.rating }
        }
    }

    fun findProduct(productId: String): Product? = allProducts.find { it.id == productId }

    fun quickAddToBag(product: Product) {
        val defaultColor = product.colors.first()
        val defaultSize = product.sizes.first()
        viewModelScope.launch {
            repository.addToBag(
                product = product,
                colorName = defaultColor.name,
                colorHex = defaultColor.hex,
                size = defaultSize,
                quantityDelta = 1
            )
        }
    }

    fun addToBag(
        product: Product,
        colorName: String,
        colorHex: Long,
        size: String,
        quantity: Int = 1
    ) {
        viewModelScope.launch {
            repository.addToBag(product, colorName, colorHex, size, quantity)
        }
    }

    fun updateCartQuantity(cartItemId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(cartItemId, quantity)
        }
    }

    fun removeCartItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromBag(cartItemId)
        }
    }

    fun toggleWishlist(productId: String) {
        val currentlySaved = wishlistIds.value.contains(productId)
        viewModelScope.launch {
            repository.toggleWishlist(productId, currentlySaved)
        }
    }

    fun applyPromoCode(code: String) {
        _uiState.update { it.copy(appliedPromoCode = code.trim().uppercase().ifBlank { null }) }
    }

    fun clearPromoCode() {
        _uiState.update { it.copy(appliedPromoCode = null) }
    }

    fun selectDeliveryMethod(method: DeliveryMethod) {
        _uiState.update { it.copy(selectedDeliveryMethod = method) }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun selectAddress(addressId: Long) {
        _uiState.update { it.copy(selectedAddressId = addressId) }
    }

    fun addAddress(
        label: String,
        fullName: String,
        street: String,
        cityStateZip: String,
        phone: String,
        makeDefault: Boolean
    ) {
        viewModelScope.launch {
            repository.addAddress(label, fullName, street, cityStateZip, phone, makeDefault)
        }
    }

    fun setDefaultAddress(id: Long) {
        viewModelScope.launch {
            repository.setDefaultAddress(id)
        }
    }

    fun deleteAddress(id: Long) {
        viewModelScope.launch {
            repository.deleteAddress(id)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkMode(enabled)
        }
    }

    fun decodeOrderItems(itemsJson: String): List<OrderLineItem> =
        repository.decodeOrderItems(itemsJson)

    fun placeOrder(onSuccess: (String) -> Unit) {
        val items = cartItems.value
        if (items.isEmpty() || _uiState.value.isPlacingOrder) return

        val addressList = addresses.value
        val activeAddress = addressList.find { it.id == _uiState.value.selectedAddressId }
            ?: addressList.firstOrNull { it.isDefault }
            ?: addressList.firstOrNull()
            ?: ShippingAddressEntity(
                label = "Studio Home",
                fullName = "Alex Mercer",
                street = "742 Evergreen Terrace, Apt 4B",
                cityStateZip = "Portland, OR 97205",
                phone = "(503) 555-0194",
                isDefault = true
            )

        _uiState.update { it.copy(isPlacingOrder = true) }
        viewModelScope.launch {
            val pricing = checkoutPricing.value
            val orderId = repository.placeOrder(
                items = items,
                address = activeAddress,
                deliveryMethod = _uiState.value.selectedDeliveryMethod,
                paymentMethod = _uiState.value.selectedPaymentMethod,
                pricing = pricing
            )
            _uiState.update {
                it.copy(
                    isPlacingOrder = false,
                    appliedPromoCode = null
                )
            }
            onSuccess(orderId)
        }
    }
}

class ShopViewModelFactory(
    private val repository: ShopRepository,
    private val themePreferences: ThemePreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
            return ShopViewModel(repository, themePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
