package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.data.local.KansoDatabase
import com.example.data.preferences.ThemePreferences
import com.example.data.repository.ShopRepository
import com.example.ui.navigation.BagRoute
import com.example.ui.navigation.CheckoutRoute
import com.example.ui.navigation.DiscoverRoute
import com.example.ui.navigation.ExploreRoute
import com.example.ui.navigation.OrderSuccessRoute
import com.example.ui.navigation.ProductDetailRoute
import com.example.ui.navigation.ProfileRoute
import com.example.ui.navigation.SavedRoute
import com.example.ui.screens.BagScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.KansoTheme
import com.example.ui.viewmodel.ShopViewModel
import com.example.ui.viewmodel.ShopViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = KansoDatabase.getInstance(applicationContext)
        val repository = ShopRepository(database.shopDao())
        val themePreferences = ThemePreferences(applicationContext)

        setContent {
            val viewModel: ShopViewModel = viewModel(
                factory = ShopViewModelFactory(repository, themePreferences)
            )
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

            KansoTheme(darkTheme = isDarkMode) {
                KansoApp(viewModel = viewModel, isDarkMode = isDarkMode)
            }
        }
    }
}

private data class TopLevelDestination<T : Any>(
    val route: T,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun KansoApp(
    viewModel: ShopViewModel,
    isDarkMode: Boolean
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val addresses by viewModel.addresses.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val bagPricing by viewModel.bagPricing.collectAsStateWithLifecycle()
    val checkoutPricing by viewModel.checkoutPricing.collectAsStateWithLifecycle()
    val bagItemCount by viewModel.bagItemCount.collectAsStateWithLifecycle()

    val topDestinations = remember {
        listOf(
            TopLevelDestination(
                route = DiscoverRoute,
                labelRes = R.string.nav_discover,
                selectedIcon = Icons.Filled.Storefront,
                unselectedIcon = Icons.Outlined.Storefront,
                testTag = "nav_discover"
            ),
            TopLevelDestination(
                route = ExploreRoute,
                labelRes = R.string.nav_explore,
                selectedIcon = Icons.Filled.Explore,
                unselectedIcon = Icons.Outlined.Explore,
                testTag = "nav_explore"
            ),
            TopLevelDestination(
                route = SavedRoute,
                labelRes = R.string.nav_saved,
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder,
                testTag = "nav_saved"
            ),
            TopLevelDestination(
                route = BagRoute,
                labelRes = R.string.nav_bag,
                selectedIcon = Icons.Filled.ShoppingBag,
                unselectedIcon = Icons.Outlined.ShoppingBag,
                testTag = "nav_bag"
            ),
            TopLevelDestination(
                route = ProfileRoute,
                labelRes = R.string.nav_orders,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                testTag = "nav_profile"
            )
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showTopLevelNav = currentDestination?.let { dest ->
        dest.hasRoute<DiscoverRoute>() ||
            dest.hasRoute<ExploreRoute>() ||
            dest.hasRoute<SavedRoute>() ||
            dest.hasRoute<BagRoute>() ||
            dest.hasRoute<ProfileRoute>()
    } ?: true

    fun navigateTopLevel(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun showAddedToBagSnackbar(productName: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = "$productName added to bag",
                actionLabel = "View Bag",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                navigateTopLevel(BagRoute)
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = if (showTopLevelNav) {
                WindowInsets(0, 0, 0, 0)
            } else {
                WindowInsets(0, 0, 0, 0)
            },
            bottomBar = {
                if (showTopLevelNav && !useNavigationRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        tonalElevation = 2.dp
                    ) {
                        topDestinations.forEach { dest ->
                            val selected = currentDestination?.hasRoute(dest.route::class) == true
                            val label = stringResource(dest.labelRes)
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    NavDestinationIcon(
                                        dest = dest,
                                        selected = selected,
                                        bagCount = bagItemCount,
                                        savedCount = wishlistIds.size
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag(dest.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTopLevelNav && useNavigationRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        topDestinations.forEach { dest ->
                            val selected = currentDestination?.hasRoute(dest.route::class) == true
                            val label = stringResource(dest.labelRes)
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    NavDestinationIcon(
                                        dest = dest,
                                        selected = selected,
                                        bagCount = bagItemCount,
                                        savedCount = wishlistIds.size
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag(dest.testTag)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = DiscoverRoute
                    ) {
                        composable<DiscoverRoute> {
                            DiscoverScreen(
                                products = viewModel.allProducts,
                                selectedCategory = uiState.selectedCategory,
                                wishlistIds = wishlistIds,
                                bagItemCount = bagItemCount,
                                onSelectCategory = viewModel::selectDiscoverCategory,
                                onSearchClick = { navigateTopLevel(ExploreRoute) },
                                onBagClick = { navigateTopLevel(BagRoute) },
                                onExploreCollectionClick = { navigateTopLevel(ExploreRoute) },
                                onProductClick = { product ->
                                    navController.navigate(ProductDetailRoute(product.id))
                                },
                                onToggleWishlist = { product ->
                                    viewModel.toggleWishlist(product.id)
                                },
                                onQuickAdd = { product ->
                                    viewModel.quickAddToBag(product)
                                    showAddedToBagSnackbar(product.name)
                                },
                                modifier = Modifier.statusBarsPadding()
                            )
                        }

                        composable<ExploreRoute> {
                            val filtered = remember(uiState) {
                                viewModel.getFilteredExploreProducts(uiState)
                            }
                            ExploreScreen(
                                uiState = uiState,
                                filteredProducts = filtered,
                                wishlistIds = wishlistIds,
                                onSearchQueryChange = viewModel::updateSearchQuery,
                                onSelectCategory = viewModel::selectExploreCategory,
                                onSelectSortOption = viewModel::selectSortOption,
                                onMaxPriceChange = viewModel::updateMaxPriceFilter,
                                onToggleOnlyOnSale = viewModel::toggleOnlyOnSale,
                                onResetFilters = viewModel::resetExploreFilters,
                                onProductClick = { product ->
                                    navController.navigate(ProductDetailRoute(product.id))
                                },
                                onToggleWishlist = { product ->
                                    viewModel.toggleWishlist(product.id)
                                },
                                onQuickAdd = { product ->
                                    viewModel.quickAddToBag(product)
                                    showAddedToBagSnackbar(product.name)
                                },
                                modifier = Modifier.statusBarsPadding()
                            )
                        }

                        composable<SavedRoute> {
                            SavedScreen(
                                allProducts = viewModel.allProducts,
                                wishlistIds = wishlistIds,
                                onProductClick = { product ->
                                    navController.navigate(ProductDetailRoute(product.id))
                                },
                                onMoveToBag = { product ->
                                    viewModel.quickAddToBag(product)
                                    showAddedToBagSnackbar(product.name)
                                },
                                onRemoveFromWishlist = { product ->
                                    viewModel.toggleWishlist(product.id)
                                },
                                onDiscoverClick = { navigateTopLevel(DiscoverRoute) },
                                modifier = Modifier.statusBarsPadding()
                            )
                        }

                        composable<BagRoute> {
                            BagScreen(
                                cartItems = cartItems,
                                pricing = bagPricing,
                                onProductClick = { productId ->
                                    navController.navigate(ProductDetailRoute(productId))
                                },
                                onUpdateQuantity = viewModel::updateCartQuantity,
                                onRemoveItem = viewModel::removeCartItem,
                                onApplyPromo = viewModel::applyPromoCode,
                                onClearPromo = viewModel::clearPromoCode,
                                onCheckoutClick = {
                                    navController.navigate(CheckoutRoute)
                                },
                                onDiscoverClick = { navigateTopLevel(DiscoverRoute) },
                                modifier = Modifier.statusBarsPadding()
                            )
                        }

                        composable<ProfileRoute> {
                            ProfileScreen(
                                orders = orders,
                                addresses = addresses,
                                wishlistCount = wishlistIds.size,
                                isDarkMode = isDarkMode,
                                decodeOrderItems = viewModel::decodeOrderItems,
                                onToggleDarkMode = viewModel::setDarkMode,
                                onAddAddress = { label, name, street, cityZip, phone ->
                                    viewModel.addAddress(label, name, street, cityZip, phone, true)
                                },
                                onSetDefaultAddress = viewModel::setDefaultAddress,
                                onDeleteAddress = viewModel::deleteAddress,
                                onProductClick = { productId ->
                                    navController.navigate(ProductDetailRoute(productId))
                                },
                                modifier = Modifier.statusBarsPadding()
                            )
                        }

                        composable<ProductDetailRoute> { backStackEntry ->
                            val route = backStackEntry.toRoute<ProductDetailRoute>()
                            val product = viewModel.findProduct(route.productId)
                                ?: viewModel.allProducts.first()

                            ProductDetailScreen(
                                product = product,
                                isWishlisted = wishlistIds.contains(product.id),
                                onBackClick = { navController.popBackStack() },
                                onToggleWishlist = { viewModel.toggleWishlist(product.id) },
                                onAddToBag = { colorName, colorHex, size ->
                                    viewModel.addToBag(product, colorName, colorHex, size, 1)
                                },
                                onOpenBag = {
                                    navController.popBackStack()
                                    navigateTopLevel(BagRoute)
                                }
                            )
                        }

                        composable<CheckoutRoute> {
                            CheckoutScreen(
                                cartItems = cartItems,
                                addresses = addresses,
                                selectedAddressId = uiState.selectedAddressId,
                                selectedDeliveryMethod = uiState.selectedDeliveryMethod,
                                selectedPaymentMethod = uiState.selectedPaymentMethod,
                                pricing = checkoutPricing,
                                isPlacingOrder = uiState.isPlacingOrder,
                                onBackClick = { navController.popBackStack() },
                                onSelectAddress = viewModel::selectAddress,
                                onAddAddress = { label, name, street, cityZip, phone ->
                                    viewModel.addAddress(label, name, street, cityZip, phone, true)
                                },
                                onSelectDeliveryMethod = viewModel::selectDeliveryMethod,
                                onSelectPaymentMethod = viewModel::selectPaymentMethod,
                                onPlaceOrder = {
                                    viewModel.placeOrder { orderId ->
                                        navController.navigate(OrderSuccessRoute(orderId)) {
                                            popUpTo(BagRoute) { inclusive = false }
                                        }
                                    }
                                }
                            )
                        }

                        composable<OrderSuccessRoute> { backStackEntry ->
                            val route = backStackEntry.toRoute<OrderSuccessRoute>()
                            OrderSuccessScreen(
                                orderId = route.orderId,
                                onViewOrdersClick = {
                                    navController.popBackStack(DiscoverRoute, inclusive = false)
                                    navigateTopLevel(ProfileRoute)
                                },
                                onContinueShoppingClick = {
                                    navController.popBackStack(DiscoverRoute, inclusive = false)
                                    navigateTopLevel(DiscoverRoute)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavDestinationIcon(
    dest: TopLevelDestination<*>,
    selected: Boolean,
    bagCount: Int,
    savedCount: Int
) {
    val icon = if (selected) dest.selectedIcon else dest.unselectedIcon
    val badgeCount = when (dest.route) {
        is BagRoute -> bagCount
        is SavedRoute -> savedCount
        else -> 0
    }

    if (badgeCount > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(badgeCount.toString())
                }
            }
        ) {
            Icon(imageVector = icon, contentDescription = stringResource(dest.labelRes))
        }
    } else {
        Icon(imageVector = icon, contentDescription = stringResource(dest.labelRes))
    }
}
