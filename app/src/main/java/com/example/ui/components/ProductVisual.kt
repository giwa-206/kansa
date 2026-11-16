package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.data.model.Product
import com.example.data.model.ProductVisualType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ProductVisual(
    product: Product,
    selectedColorHex: Long? = null,
    modifier: Modifier = Modifier
) {
    val accentColor = Color(selectedColorHex ?: product.colors.first().hex)
    val studioBgTop = MaterialTheme.colorScheme.surfaceContainerLow
    val studioBgBottom = MaterialTheme.colorScheme.surfaceContainerHigh

    Box(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(studioBgTop, studioBgBottom)
            )
        )
    ) {
        if (product.photoResId != null) {
            Image(
                painter = painterResource(id = product.photoResId),
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Soft architectural studio backdrop halo
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.5f, h * 0.48f),
                        radius = minOf(w, h) * 0.55f
                    ),
                    center = Offset(w * 0.5f, h * 0.48f),
                    radius = minOf(w, h) * 0.55f
                )

                // Subtle plinth shadow
                drawOval(
                    color = Color.Black.copy(alpha = 0.08f),
                    topLeft = Offset(w * 0.25f, h * 0.78f),
                    size = Size(w * 0.5f, h * 0.07f)
                )

                when (product.visualType) {
                    ProductVisualType.ILLUSTRATION_LAMP -> drawLamp(accentColor)
                    ProductVisualType.ILLUSTRATION_WATCH -> drawWatch(accentColor)
                    ProductVisualType.ILLUSTRATION_CARAFE -> drawCarafe(accentColor)
                    ProductVisualType.ILLUSTRATION_BLANKET -> drawBlanket(accentColor)
                    ProductVisualType.ILLUSTRATION_SPEAKER -> drawSpeaker(accentColor)
                    ProductVisualType.ILLUSTRATION_PEN -> drawPen(accentColor)
                    ProductVisualType.ILLUSTRATION_DIFFUSER -> drawDiffuser(accentColor)
                    ProductVisualType.ILLUSTRATION_OVERSHIRT -> drawOvershirt(accentColor)
                    ProductVisualType.ILLUSTRATION_CARDHOLDER -> drawCardholder(accentColor)
                    else -> Unit
                }
            }
        }
    }
}

private fun DrawScope.drawLamp(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Warm light cone
    val cone = Path().apply {
        moveTo(cx - w * 0.2f, h * 0.43f)
        lineTo(cx + w * 0.2f, h * 0.43f)
        lineTo(cx + w * 0.34f, h * 0.78f)
        lineTo(cx - w * 0.34f, h * 0.78f)
        close()
    }
    drawPath(
        path = cone,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFE4B5).copy(alpha = 0.45f), Color.Transparent),
            startY = h * 0.43f,
            endY = h * 0.78f
        )
    )

    // Stem
    drawLine(
        color = Color(0xFF2D2A27),
        start = Offset(cx, h * 0.42f),
        end = Offset(cx, h * 0.77f),
        strokeWidth = w * 0.032f,
        cap = StrokeCap.Round
    )

    // Weighted circular base
    drawRoundRect(
        color = Color(0xFF2D2A27),
        topLeft = Offset(cx - w * 0.16f, h * 0.74f),
        size = Size(w * 0.32f, h * 0.055f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )

    // Brass dimmer dial
    drawCircle(
        color = Color(0xFFD4A359),
        radius = w * 0.024f,
        center = Offset(cx + w * 0.09f, h * 0.74f)
    )

    // Spun steel dome shade
    drawArc(
        brush = Brush.linearGradient(
            colors = listOf(accent, accent.copy(alpha = 0.8f)),
            start = Offset(cx - w * 0.25f, h * 0.2f),
            end = Offset(cx + w * 0.25f, h * 0.45f)
        ),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(cx - w * 0.24f, h * 0.22f),
        size = Size(w * 0.48f, h * 0.42f)
    )

    // Brass finial
    drawCircle(
        color = Color(0xFFD4A359),
        radius = w * 0.022f,
        center = Offset(cx, h * 0.21f)
    )
}

private fun DrawScope.drawWatch(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.5f
    val r = minOf(w, h) * 0.21f

    // Leather strap
    drawRoundRect(
        color = Color(0xFF4B372B),
        topLeft = Offset(cx - r * 0.56f, h * 0.14f),
        size = Size(r * 1.12f, h * 0.72f),
        cornerRadius = CornerRadius(r * 0.2f, r * 0.2f)
    )

    // Titanium case outer bezel
    drawCircle(
        color = Color(0xFFBDB8B0),
        radius = r * 1.12f,
        center = Offset(cx, cy)
    )
    // Dial
    drawCircle(
        color = accent,
        radius = r * 0.94f,
        center = Offset(cx, cy)
    )
    // Crown
    drawRoundRect(
        color = Color(0xFF9C968D),
        topLeft = Offset(cx + r * 1.06f, cy - r * 0.16f),
        size = Size(r * 0.2f, r * 0.32f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Hour markers
    val markerColor = if (accent.luminanceApprox() > 0.55f) Color(0xFF23201E) else Color(0xFFF6F2EC)
    for (i in 0 until 12) {
        val angle = Math.toRadians((i * 30).toDouble())
        val inner = r * 0.76f
        val outer = r * 0.87f
        drawLine(
            color = markerColor.copy(alpha = if (i % 3 == 0) 0.9f else 0.45f),
            start = Offset(cx + (cos(angle) * inner).toFloat(), cy + (sin(angle) * inner).toFloat()),
            end = Offset(cx + (cos(angle) * outer).toFloat(), cy + (sin(angle) * outer).toFloat()),
            strokeWidth = if (i % 3 == 0) 3.5f else 2f,
            cap = StrokeCap.Round
        )
    }
    // Hands
    drawLine(
        color = markerColor,
        start = Offset(cx, cy),
        end = Offset(cx - r * 0.36f, cy - r * 0.32f),
        strokeWidth = 4.5f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = markerColor,
        start = Offset(cx, cy),
        end = Offset(cx + r * 0.52f, cy - r * 0.18f),
        strokeWidth = 3.2f,
        cap = StrokeCap.Round
    )
    drawCircle(color = Color(0xFFC45B3B), radius = r * 0.08f, center = Offset(cx, cy))
}

private fun DrawScope.drawCarafe(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    val bodyPath = Path().apply {
        moveTo(cx - w * 0.1f, h * 0.24f)
        lineTo(cx + w * 0.1f, h * 0.24f)
        lineTo(cx + w * 0.17f, h * 0.48f)
        lineTo(cx + w * 0.17f, h * 0.76f)
        lineTo(cx - w * 0.17f, h * 0.76f)
        lineTo(cx - w * 0.17f, h * 0.48f)
        close()
    }
    drawPath(
        path = bodyPath,
        brush = Brush.horizontalGradient(
            colors = listOf(
                accent.copy(alpha = 0.42f),
                accent.copy(alpha = 0.18f),
                accent.copy(alpha = 0.48f)
            )
        )
    )
    drawPath(
        path = bodyPath,
        color = accent.copy(alpha = 0.75f),
        style = Stroke(width = 3f)
    )

    // Vertical fluted ribs
    for (offsetFactor in listOf(-0.09f, -0.03f, 0.03f, 0.09f)) {
        drawLine(
            color = Color.White.copy(alpha = 0.42f),
            start = Offset(cx + w * offsetFactor, h * 0.48f),
            end = Offset(cx + w * offsetFactor, h * 0.74f),
            strokeWidth = 2.5f
        )
    }

    // Nested glass tumbler cap
    drawRoundRect(
        color = accent.copy(alpha = 0.35f),
        topLeft = Offset(cx - w * 0.11f, h * 0.17f),
        size = Size(w * 0.22f, h * 0.17f),
        cornerRadius = CornerRadius(10f, 10f)
    )
    drawRoundRect(
        color = accent.copy(alpha = 0.8f),
        topLeft = Offset(cx - w * 0.11f, h * 0.17f),
        size = Size(w * 0.22f, h * 0.17f),
        cornerRadius = CornerRadius(10f, 10f),
        style = Stroke(width = 2.5f)
    )
}

private fun DrawScope.drawBlanket(accent: Color) {
    val w = size.width
    val h = size.height
    val left = w * 0.24f
    val top = h * 0.26f
    val bw = w * 0.52f
    val bh = h * 0.48f

    // Folded back layer
    drawRoundRect(
        color = accent.copy(alpha = 0.75f),
        topLeft = Offset(left + w * 0.04f, top - h * 0.04f),
        size = Size(bw - w * 0.04f, bh),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    // Front folded throw
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(accent, accent.copy(alpha = 0.88f))
        ),
        topLeft = Offset(left, top),
        size = Size(bw, bh),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )

    // Herringbone weave lines
    val stripeColor = if (accent.luminanceApprox() > 0.55f) {
        Color.Black.copy(alpha = 0.12f)
    } else {
        Color.White.copy(alpha = 0.16f)
    }
    for (row in 1..5) {
        val y = top + bh * (row / 6f)
        drawLine(
            color = stripeColor,
            start = Offset(left + w * 0.04f, y),
            end = Offset(left + bw - w * 0.04f, y),
            strokeWidth = 2.5f
        )
    }

    // Fringe tassels on bottom
    for (i in 0..9) {
        val x = left + w * 0.04f + i * ((bw - w * 0.08f) / 9f)
        drawLine(
            color = Color(0xFFE6DDD0),
            start = Offset(x, top + bh),
            end = Offset(x, top + bh + h * 0.055f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawSpeaker(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Machined aluminum top cap
    drawRoundRect(
        color = Color(0xFFD6D1C9),
        topLeft = Offset(cx - w * 0.24f, h * 0.25f),
        size = Size(w * 0.48f, h * 0.08f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )
    // Rotary dial knob
    drawRoundRect(
        color = Color(0xFF3E3B38),
        topLeft = Offset(cx + w * 0.08f, h * 0.215f),
        size = Size(w * 0.09f, h * 0.04f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Acoustic woven fabric cabinet
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(accent, accent.copy(alpha = 0.82f))
        ),
        topLeft = Offset(cx - w * 0.24f, h * 0.30f),
        size = Size(w * 0.48f, h * 0.46f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )

    // Subtle acoustic woofer ring silhouette
    drawCircle(
        color = Color.Black.copy(alpha = 0.14f),
        radius = w * 0.13f,
        center = Offset(cx, h * 0.54f),
        style = Stroke(width = 4f)
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.16f),
        radius = w * 0.045f,
        center = Offset(cx, h * 0.54f)
    )
}

private fun DrawScope.drawPen(accent: Color) {
    val w = size.width
    val h = size.height
    val start = Offset(w * 0.28f, h * 0.72f)
    val end = Offset(w * 0.72f, h * 0.24f)

    // Pen barrel
    drawLine(
        color = accent,
        start = start,
        end = end,
        strokeWidth = w * 0.055f,
        cap = StrokeCap.Round
    )
    // Highlight ridge
    drawLine(
        color = Color.White.copy(alpha = 0.38f),
        start = Offset(start.x - 3f, start.y - 3f),
        end = Offset(end.x - 3f, end.y - 3f),
        strokeWidth = w * 0.014f,
        cap = StrokeCap.Round
    )
    // Machined tip cone
    drawCircle(
        color = Color(0xFF232120),
        radius = w * 0.016f,
        center = Offset(w * 0.245f, h * 0.755f)
    )
    // Click plunger
    drawLine(
        color = Color(0xFF2D2A27),
        start = end,
        end = Offset(w * 0.755f, h * 0.205f),
        strokeWidth = w * 0.032f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawDiffuser(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Mist plume
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.65f), Color.Transparent),
            center = Offset(cx, h * 0.23f),
            radius = w * 0.14f
        ),
        radius = w * 0.14f,
        center = Offset(cx, h * 0.23f)
    )

    // Cedar wood base ring
    drawRoundRect(
        color = Color(0xFF9C7248),
        topLeft = Offset(cx - w * 0.17f, h * 0.71f),
        size = Size(w * 0.34f, h * 0.06f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )

    // Porcelain sculptural vessel
    val vessel = Path().apply {
        moveTo(cx - w * 0.06f, h * 0.32f)
        lineTo(cx + w * 0.06f, h * 0.32f)
        cubicTo(
            cx + w * 0.21f, h * 0.45f,
            cx + w * 0.21f, h * 0.68f,
            cx + w * 0.15f, h * 0.73f
        )
        lineTo(cx - w * 0.15f, h * 0.73f)
        cubicTo(
            cx - w * 0.21f, h * 0.68f,
            cx - w * 0.21f, h * 0.45f,
            cx - w * 0.06f, h * 0.32f
        )
        close()
    }
    drawPath(
        path = vessel,
        brush = Brush.horizontalGradient(
            colors = listOf(accent, accent.copy(alpha = 0.84f))
        )
    )
}

private fun DrawScope.drawOvershirt(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    val garment = Path().apply {
        moveTo(cx - w * 0.09f, h * 0.22f)
        lineTo(cx + w * 0.09f, h * 0.22f)
        lineTo(cx + w * 0.28f, h * 0.32f)
        lineTo(cx + w * 0.33f, h * 0.62f)
        lineTo(cx + w * 0.23f, h * 0.64f)
        lineTo(cx + w * 0.20f, h * 0.44f)
        lineTo(cx + w * 0.20f, h * 0.77f)
        lineTo(cx - w * 0.20f, h * 0.77f)
        lineTo(cx - w * 0.20f, h * 0.44f)
        lineTo(cx - w * 0.23f, h * 0.64f)
        lineTo(cx - w * 0.33f, h * 0.62f)
        lineTo(cx - w * 0.28f, h * 0.32f)
        close()
    }
    drawPath(path = garment, color = accent)

    // Placket line & corozo buttons
    drawLine(
        color = Color.Black.copy(alpha = 0.18f),
        start = Offset(cx, h * 0.26f),
        end = Offset(cx, h * 0.77f),
        strokeWidth = 2.5f
    )
    for (yRatio in listOf(0.35f, 0.45f, 0.55f, 0.65f)) {
        drawCircle(
            color = Color(0xFFDFC7A9),
            radius = w * 0.014f,
            center = Offset(cx, h * yRatio)
        )
    }
    // Patch pocket
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.1f),
        topLeft = Offset(cx + w * 0.05f, h * 0.42f),
        size = Size(w * 0.1f, h * 0.12f),
        cornerRadius = CornerRadius(4f, 4f),
        style = Stroke(width = 2.5f)
    )
}

private fun DrawScope.drawCardholder(accent: Color) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Peeked card
    drawRoundRect(
        color = Color(0xFFFAF8F5),
        topLeft = Offset(cx - w * 0.21f, h * 0.27f),
        size = Size(w * 0.42f, h * 0.28f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )
    // Leather body
    drawRoundRect(
        color = accent,
        topLeft = Offset(cx - w * 0.25f, h * 0.36f),
        size = Size(w * 0.5f, h * 0.39f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    // Diagonal pocket fold
    val fold = Path().apply {
        moveTo(cx - w * 0.25f, h * 0.48f)
        lineTo(cx + w * 0.25f, h * 0.40f)
        lineTo(cx + w * 0.25f, h * 0.75f)
        lineTo(cx - w * 0.25f, h * 0.75f)
        close()
    }
    drawPath(path = fold, color = accent.copy(alpha = 0.85f))
    // Waxed linen stitch line
    drawRoundRect(
        color = Color(0xFFF3ECE0).copy(alpha = 0.55f),
        topLeft = Offset(cx - w * 0.22f, h * 0.39f),
        size = Size(w * 0.44f, h * 0.33f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f),
        style = Stroke(width = 2f)
    )
}

private fun Color.luminanceApprox(): Float = 0.299f * red + 0.587f * green + 0.114f * blue
