package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.BagPricingSummary
import com.example.data.model.CartItemWithProduct
import com.example.ui.components.ProductVisual
import com.example.ui.components.formatCurrency
import com.example.ui.components.formatCurrencyExact

@Composable
fun BagScreen(
    cartItems: List<CartItemWithProduct>,
    pricing: BagPricingSummary,
    onProductClick: (String) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onApplyPromo: (String) -> Unit,
    onClearPromo: () -> Unit,
    onCheckoutClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var promoInput by remember(pricing.appliedPromoCode) {
        mutableStateOf(pricing.appliedPromoCode ?: "")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Shopping Bag",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val totalCount = cartItems.sumOf { it.quantity }
                    Text(
                        text = "$totalCount ${if (totalCount == 1) "object" else "objects"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (cartItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.empty_bag_title),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.empty_bag_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 320.dp)
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Button(
                        onClick = onDiscoverClick,
                        shape = CircleShape,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("bag_discover_cta")
                    ) {
                        Text(stringResource(R.string.action_discover_objects))
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Free shipping progress indicator
                    item {
                        val progress = ((pricing.subtotal / 150.0).coerceIn(0.0, 1.0)).toFloat()
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.LocalShipping,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (pricing.amountForFreeShipping <= 0.0 || pricing.shippingFee == 0.0) {
                                            "You unlocked complimentary standard shipping"
                                        } else {
                                            "Add ${formatCurrency(pricing.amountForFreeShipping)} more for free shipping"
                                        },
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                            }
                        }
                    }

                    // Cart line items
                    items(cartItems, key = { it.cartItemId }) { item ->
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("bag_item_${item.product.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProductVisual(
                                    product = item.product,
                                    selectedColorHex = item.selectedColorHex,
                                    modifier = Modifier
                                        .size(94.dp)
                                        .clip(MaterialTheme.shapes.small)
                                        .clickable { onProductClick(item.product.id) }
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = item.product.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { onProductClick(item.product.id) }
                                        )
                                        IconButton(
                                            onClick = { onRemoveItem(item.cartItemId) },
                                            modifier = Modifier
                                                .minimumInteractiveComponentSize()
                                                .testTag("remove_bag_item_${item.product.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.DeleteOutline,
                                                contentDescription = "Remove ${item.product.name}",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(Color(item.selectedColorHex))
                                                .border(0.5.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                        )
                                        Text(
                                            text = "${item.selectedColorName} • ${item.selectedSize}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Quantity stepper
                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.surfaceContainer,
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                IconButton(
                                                    onClick = {
                                                        onUpdateQuantity(item.cartItemId, item.quantity - 1)
                                                    },
                                                    modifier = Modifier
                                                        .minimumInteractiveComponentSize()
                                                        .testTag("qty_minus_${item.product.id}")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Remove,
                                                        contentDescription = "Decrease quantity",
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                                Text(
                                                    text = item.quantity.toString(),
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    modifier = Modifier.padding(horizontal = 4.dp)
                                                )
                                                IconButton(
                                                    onClick = {
                                                        onUpdateQuantity(item.cartItemId, item.quantity + 1)
                                                    },
                                                    modifier = Modifier
                                                        .minimumInteractiveComponentSize()
                                                        .testTag("qty_plus_${item.product.id}")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Add,
                                                        contentDescription = "Increase quantity",
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = formatCurrency(item.lineTotal),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Promo Code Section
                    item {
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.LocalOffer,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Promo Code",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = promoInput,
                                        onValueChange = { promoInput = it.uppercase() },
                                        placeholder = { Text("Enter code (e.g. KANSO15)") },
                                        singleLine = true,
                                        trailingIcon = {
                                            if (pricing.appliedPromoCode != null) {
                                                IconButton(
                                                    onClick = {
                                                        promoInput = ""
                                                        onClearPromo()
                                                    }
                                                ) {
                                                    Icon(Icons.Filled.Close, contentDescription = "Clear promo")
                                                }
                                            }
                                        },
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("promo_code_input")
                                    )
                                    OutlinedButton(
                                        onClick = { onApplyPromo(promoInput) },
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier
                                            .height(54.dp)
                                            .testTag("apply_promo_button")
                                    ) {
                                        Text("Apply")
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("KANSO15", "FREESHIP").forEach { code ->
                                        Surface(
                                            onClick = {
                                                promoInput = code
                                                onApplyPromo(code)
                                            },
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.surfaceContainer,
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                        ) {
                                            Text(
                                                text = "Tap to use $code",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                pricing.promoMessage?.let { msg ->
                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (pricing.appliedPromoCode != null) {
                                            MaterialTheme.colorScheme.secondary
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Order Summary Card
                    item {
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Order Summary",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                SummaryLine("Subtotal", formatCurrencyExact(pricing.subtotal))
                                SummaryLine(
                                    label = "Shipping",
                                    value = if (pricing.shippingFee == 0.0) "FREE" else formatCurrencyExact(pricing.shippingFee),
                                    highlight = pricing.shippingFee == 0.0
                                )
                                if (pricing.discountAmount > 0.0) {
                                    SummaryLine(
                                        label = "Discount (${pricing.appliedPromoCode})",
                                        value = "-${formatCurrencyExact(pricing.discountAmount)}",
                                        highlight = true
                                    )
                                }
                                SummaryLine("Estimated Tax (8%)", formatCurrencyExact(pricing.estimatedTax))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = formatCurrencyExact(pricing.total),
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Button(
                                    onClick = onCheckoutClick,
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp)
                                        .testTag("proceed_to_checkout_button")
                                ) {
                                    Text(
                                        text = stringResource(R.string.action_checkout),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryLine(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = if (highlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
        )
    }
}
