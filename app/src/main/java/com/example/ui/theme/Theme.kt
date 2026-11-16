package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KansoDarkColorScheme = darkColorScheme(
    primary = TerracottaLight,
    onPrimary = CharcoalInk,
    primaryContainer = TerracottaDarkContainer,
    onPrimaryContainer = OnTerracottaDarkContainer,
    secondary = SageLight,
    onSecondary = CharcoalInk,
    secondaryContainer = SageDarkContainer,
    onSecondaryContainer = OnSageDarkContainer,
    tertiary = GoldLight,
    onTertiary = CharcoalInk,
    tertiaryContainer = GoldDarkContainer,
    onTertiaryContainer = OnGoldDarkContainer,
    background = ObsidianBg,
    onBackground = IvoryText,
    surface = EspressoSurface,
    onSurface = IvoryText,
    surfaceVariant = WarmSlateContainer,
    onSurfaceVariant = WarmTaupeText,
    surfaceContainerLowest = ObsidianBg,
    surfaceContainerLow = EspressoSurface,
    surfaceContainer = ElevatedCharcoalCard,
    surfaceContainerHigh = WarmSlateContainer,
    surfaceContainerHighest = Color(0xFF34302B),
    outline = DarkHairline,
    outlineVariant = Color(0xFF2B2723),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

private val KansoLightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = PureIvoryCard,
    primaryContainer = TerracottaContainer,
    onPrimaryContainer = OnTerracottaContainer,
    secondary = ForestSageSecondary,
    onSecondary = PureIvoryCard,
    secondaryContainer = ForestSageContainer,
    onSecondaryContainer = OnForestSageContainer,
    tertiary = WarmGoldTertiary,
    onTertiary = PureIvoryCard,
    tertiaryContainer = WarmGoldContainer,
    onTertiaryContainer = OnWarmGoldContainer,
    background = AlabasterBg,
    onBackground = CharcoalInk,
    surface = WarmSandSurface,
    onSurface = CharcoalInk,
    surfaceVariant = SoftStoneContainer,
    onSurfaceVariant = MutedUmber,
    surfaceContainerLowest = PureIvoryCard,
    surfaceContainerLow = AlabasterBg,
    surfaceContainer = WarmSandSurface,
    surfaceContainerHigh = SoftStoneContainer,
    surfaceContainerHighest = Color(0xFFE0D6C8),
    outline = SubtleHairline,
    outlineVariant = Color(0xFFE8E0D4),
    error = CrimsonError,
    onError = PureIvoryCard,
    errorContainer = CrimsonErrorContainer
)

@Composable
fun KansoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) KansoDarkColorScheme else KansoLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KansoTypography,
        shapes = KansoShapes,
        content = content
    )
}
