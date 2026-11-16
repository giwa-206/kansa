package com.example.data.local

import com.example.R
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.ProductColorOption
import com.example.data.model.ProductReview
import com.example.data.model.ProductVisualType

object CatalogDataSource {

    val products: List<Product> = listOf(
        Product(
            id = "kyoto-pour-over",
            name = "Kyoto Stoneware Pour-Over",
            subtitle = "Hand-thrown matte carafe & cup",
            category = ProductCategory.HOME,
            price = 78.0,
            originalPrice = 94.0,
            rating = 4.9,
            reviewCount = 128,
            description = "Wheel-thrown in small batches from mineral-rich clay and finished in a velvety satin glaze. Designed to retain brewing heat while offering a tactile, unhurried morning pour.",
            materialsAndSpecs = "High-fired stoneware • 500ml carafe + 180ml vessel • Dishwasher & microwave safe • Lead-free mineral glaze",
            shippingInfo = "Ships in plastic-free molded pulp packaging within 24 hours.",
            badge = "Best Seller",
            isFeatured = true,
            visualType = ProductVisualType.PHOTO_CERAMICS,
            photoResId = R.drawable.img_prod_ceramics,
            colors = listOf(
                ProductColorOption("Chalk", 0xFFEBE4DA),
                ProductColorOption("Terracotta", 0xFFC45B3B),
                ProductColorOption("Charcoal", 0xFF2E2A27)
            ),
            sizes = listOf("500 ml Set", "800 ml Carafe"),
            reviews = listOf(
                ProductReview("Elena R.", 5, "2 days ago", "The glaze feels incredible in hand and holds heat noticeably longer than glass."),
                ProductReview("Marcus T.", 5, "1 week ago", "Clean pour with zero dripping. Instantly elevated my morning coffee ritual.")
            )
        ),
        Product(
            id = "atelier-anc-headphones",
            name = "Atelier Reference ANC",
            subtitle = "Brushed aluminum & walnut acoustics",
            category = ProductCategory.TECH,
            price = 329.0,
            originalPrice = 379.0,
            rating = 4.8,
            reviewCount = 214,
            description = "Precision-milled anodized aluminum yokes meet FSC-certified walnut cups and supple lambskin cushions. Delivers transparent studio tuning with adaptive quietness.",
            materialsAndSpecs = "40mm beryllium drivers • 45-hour battery • USB-C lossless audio • Bluetooth 5.4 multipoint • 295g",
            shippingInfo = "Complimentary express courier & 2-year studio hardware warranty.",
            badge = "Editor's Pick",
            isFeatured = true,
            visualType = ProductVisualType.PHOTO_AUDIO,
            photoResId = R.drawable.img_prod_audio,
            colors = listOf(
                ProductColorOption("Champagne", 0xFFD9C7B2),
                ProductColorOption("Obsidian", 0xFF232120),
                ProductColorOption("Sage", 0xFF567367)
            ),
            sizes = listOf("Standard Edition", "With Stand"),
            reviews = listOf(
                ProductReview("Sora K.", 5, "3 days ago", "Warm, balanced soundstage and zero clamping fatigue after 6 hours of design work."),
                ProductReview("Devon P.", 4, "2 weeks ago", "Materials look even better in person. The walnut accent is super subtle.")
            )
        ),
        Product(
            id = "atelier-architect-tote",
            name = "Saddle & Canvas Tote",
            subtitle = "Vegetable-tanned bridle leather trim",
            category = ProductCategory.CARRY,
            price = 215.0,
            originalPrice = null,
            rating = 4.9,
            reviewCount = 89,
            description = "Built to stand upright on its own. Heavy 18oz Japanese cotton duck paired with full-grain Italian bridle leather handles that develop a rich amber patina over years of carry.",
            materialsAndSpecs = "18oz organic canvas • Full-grain Italian leather • Fits 16\" laptop • Internal brass key leash • 42 × 34 × 14 cm",
            shippingInfo = "Includes dust bag. Free shipping on orders over $150.",
            badge = "New Arrival",
            isFeatured = true,
            visualType = ProductVisualType.PHOTO_TOTE,
            photoResId = R.drawable.img_prod_tote,
            colors = listOf(
                ProductColorOption("Cognac", 0xFFB86538),
                ProductColorOption("Espresso", 0xFF3E2C22),
                ProductColorOption("Forest", 0xFF2D4F43)
            ),
            sizes = listOf("Medium (14\")", "Large (16\")"),
            reviews = listOf(
                ProductReview("Claire H.", 5, "5 days ago", "Structured enough for studio visits and soft enough for weekend travel.")
            )
        ),
        Product(
            id = "bauhaus-desk-lamp",
            name = "Solis Dome Desk Lamp",
            subtitle = "Spun steel architectural task light",
            category = ProductCategory.HOME,
            price = 165.0,
            originalPrice = 195.0,
            rating = 4.7,
            reviewCount = 64,
            description = "A softly diffused downward wash of 2700K warm light without glare. Features a weighted cast-iron base and stepless brass rotary dimmer.",
            materialsAndSpecs = "Powder-coated spun steel • Solid brass dimmer • Integrated 95+ CRI LED (2700K) • Braided linen cord 2m",
            shippingInfo = "Ships safely in custom recycled foam cradle.",
            badge = "Limited",
            isFeatured = true,
            visualType = ProductVisualType.ILLUSTRATION_LAMP,
            colors = listOf(
                ProductColorOption("Terracotta", 0xFFC45B3B),
                ProductColorOption("Bone", 0xFFEAE3D5),
                ProductColorOption("Matte Black", 0xFF242220)
            ),
            sizes = listOf("Desk (38 cm)", "Floor (135 cm)"),
            reviews = listOf(
                ProductReview("Julian M.", 5, "1 week ago", "The brass dimmer clicks with real weight and the light tone is cozy all evening.")
            )
        ),
        Product(
            id = "kanso-chronograph",
            name = "Series 02 Field Timepiece",
            subtitle = "Sapphire crystal & brushed titanium",
            category = ProductCategory.CARRY,
            price = 285.0,
            originalPrice = null,
            rating = 4.9,
            reviewCount = 112,
            description = "Stripped of decorative noise. Domed anti-reflective sapphire glass frames a bead-blasted dial with recessed sub-seconds and quick-release Italian calfskin strap.",
            materialsAndSpecs = "Grade 2 Titanium 38mm case • Sapphire crystal • Japanese high-precision movement • 5 ATM water resistance",
            shippingInfo = "Presented in cedar keepsake box with 5-year movement coverage.",
            badge = null,
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_WATCH,
            colors = listOf(
                ProductColorOption("Sand Dial", 0xFFD9C9B4),
                ProductColorOption("Slate Dial", 0xFF3E4448),
                ProductColorOption("Sage Dial", 0xFF456356)
            ),
            sizes = listOf("38 mm", "41 mm"),
            reviews = listOf(
                ProductReview("Noah B.", 5, "4 days ago", "Featherlight titanium case and remarkably legible in low light.")
            )
        ),
        Product(
            id = "fluted-glass-carafe",
            name = "Fluted Borosilicate Carafe",
            subtitle = "Mouth-blown bedside water set",
            category = ProductCategory.HOME,
            price = 52.0,
            originalPrice = 64.0,
            rating = 4.8,
            reviewCount = 95,
            description = "Architectural vertical fluting refracts morning light across dining tables or nightstands. The tumbler nests seamlessly over the spout as a dust-proof lid.",
            materialsAndSpecs = "Lead-free borosilicate glass • 900ml carafe + 240ml tumbler • Heat resistant to 150°C",
            shippingInfo = "Double-boxed break-free guarantee.",
            badge = null,
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_CARAFE,
            colors = listOf(
                ProductColorOption("Amber Tint", 0xFFD4A359),
                ProductColorOption("Clear Mist", 0xFFD8E1E3),
                ProductColorOption("Smoke", 0xFF6B6A68)
            ),
            sizes = listOf("900 ml Set"),
            reviews = listOf(
                ProductReview("Maya L.", 5, "6 days ago", "Such a simple pleasure to keep on my desk. Glass feels crisp yet sturdy.")
            )
        ),
        Product(
            id = "merino-rib-throw",
            name = "Herringbone Merino Throw",
            subtitle = "Unbleached extra-fine wool",
            category = ProductCategory.HOME,
            price = 148.0,
            originalPrice = null,
            rating = 4.9,
            reviewCount = 73,
            description = "Loomed in a family-owned mill using 100% ethically sourced merino wool. Breathable warmth with hand-knotted fringe edges.",
            materialsAndSpecs = "100% Extra-fine Merino (19.5 micron) • 140 × 200 cm • OEKO-TEX certified",
            shippingInfo = "Ships in reusable cotton storage tote.",
            badge = "Cozy Edit",
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_BLANKET,
            colors = listOf(
                ProductColorOption("Oatmeal", 0xFFE6DDD0),
                ProductColorOption("Rust Clay", 0xFFB85B3E),
                ProductColorOption("Pine", 0xFF2D4F43)
            ),
            sizes = listOf("Throw (140×200)", "Bed (180×240)"),
            reviews = listOf(
                ProductReview("Hannah W.", 5, "2 weeks ago", "Zero itchiness, drapes beautifully over our linen sofa.")
            )
        ),
        Product(
            id = "monolith-wireless-speaker",
            name = "Monolith Acoustic Block",
            subtitle = "Woven acoustic textile & aluminum",
            category = ProductCategory.TECH,
            price = 249.0,
            originalPrice = 289.0,
            rating = 4.7,
            reviewCount = 58,
            description = "Designed to disappear into bookshelves while filling rooms with warm, omnidirectional acoustic clarity. Tactile machined aluminum volume wheel on top.",
            materialsAndSpecs = "Dual 3.5\" woofers + silk dome tweeter • Wi-Fi AirPlay & Bluetooth 5.3 • 18-hour portable battery",
            shippingInfo = "Free ground shipping & 30-day home trial.",
            badge = "Save 14%",
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_SPEAKER,
            colors = listOf(
                ProductColorOption("Stone Gray", 0xFF9E988E),
                ProductColorOption("Charcoal", 0xFF282624),
                ProductColorOption("Warm Sand", 0xFFD6C7B2)
            ),
            sizes = listOf("Compact", "Studio Stereo Pair"),
            reviews = listOf(
                ProductReview("Liam G.", 5, "1 week ago", "The physical volume dial is addictive and bass response is surprisingly deep.")
            )
        ),
        Product(
            id = "machined-brass-pen",
            name = "Hex Machined Brass Pen",
            subtitle = "Unlacquered solid C360 brass",
            category = ProductCategory.TECH,
            price = 58.0,
            originalPrice = null,
            rating = 4.9,
            reviewCount = 142,
            description = "CNC-turned from a single bar of solid brass with a silent ceramic click mechanism. Balances effortlessly in hand and develops an individual patina.",
            materialsAndSpecs = "Solid C360 brass • 38g balanced weight • Accepts Schmidt P900 / Parker G2 refills",
            shippingInfo = "Ships with 2 archival black ink refills.",
            badge = null,
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_PEN,
            colors = listOf(
                ProductColorOption("Raw Brass", 0xFFCBA052),
                ProductColorOption("Copper", 0xFFC46D4E),
                ProductColorOption("Graphite", 0xFF42403E)
            ),
            sizes = listOf("0.5 mm Fine", "0.7 mm Medium"),
            reviews = listOf(
                ProductReview("Owen C.", 5, "3 days ago", "Best weight balance of any everyday pen I own.")
            )
        ),
        Product(
            id = "hinoki-stone-diffuser",
            name = "Hinoki Ultrasonic Diffuser",
            subtitle = "Porous matte porcelain & cedar",
            category = ProductCategory.HOME,
            price = 96.0,
            originalPrice = null,
            rating = 4.8,
            reviewCount = 81,
            description = "Sculptural slip-cast porcelain vessel pairs whisper-quiet ultrasonic misting with a warm ambient glow ring for calm evening routines.",
            materialsAndSpecs = "Matte bisque porcelain • 160ml reservoir • 4h / 8h auto-shutoff • Includes 10ml Japanese Hinoki oil",
            shippingInfo = "Ready to ship same business day.",
            badge = "Wellness",
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_DIFFUSER,
            colors = listOf(
                ProductColorOption("Alabaster", 0xFFF0EAE0),
                ProductColorOption("Clay", 0xFFC4694D)
            ),
            sizes = listOf("Diffuser + Oil"),
            reviews = listOf(
                ProductReview("Nina S.", 5, "5 days ago", "Looks like a gallery sculpture even when turned off.")
            )
        ),
        Product(
            id = "heavy-linen-overshirt",
            name = "Belgian Linen Overshirt",
            subtitle = "Garment-washed relaxed tailoring",
            category = ProductCategory.APPAREL,
            price = 158.0,
            originalPrice = 185.0,
            rating = 4.8,
            reviewCount = 67,
            description = "Cut in a boxy architectural silhouette from heavyweight 260gsm Belgian flax linen. Finished with corozo nut buttons and deep patch pockets.",
            materialsAndSpecs = "100% Belgian Flax Linen (260gsm) • Natural corozo nut buttons • Pre-washed for zero shrinkage",
            shippingInfo = "Free returns & size exchanges within 30 days.",
            badge = "Seasonal",
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_OVERSHIRT,
            colors = listOf(
                ProductColorOption("Olive Sage", 0xFF4A5D4E),
                ProductColorOption("Ecru", 0xFFE8E0D2),
                ProductColorOption("Espresso", 0xFF3E322B)
            ),
            sizes = listOf("S", "M", "L", "XL"),
            reviews = listOf(
                ProductReview("Theo V.", 5, "1 week ago", "Substantial drape—works like a light jacket over tees.")
            )
        ),
        Product(
            id = "bridle-cardholder",
            name = "Folded Bridle Cardholder",
            subtitle = "Single-piece stitched pocket wallet",
            category = ProductCategory.APPAREL,
            price = 68.0,
            originalPrice = null,
            rating = 4.9,
            reviewCount = 104,
            description = "Origami-inspired folded pattern minimizes seams and bulk. Hand-burnished edges and waxed linen thread hold up to 8 cards plus folded bills.",
            materialsAndSpecs = "Full-grain vegetable-tanned leather • Waxed French linen thread • 10 × 7 × 0.8 cm",
            shippingInfo = "Includes cotton gift pouch.",
            badge = null,
            isFeatured = false,
            visualType = ProductVisualType.ILLUSTRATION_CARDHOLDER,
            colors = listOf(
                ProductColorOption("Saddle Tan", 0xFFB86B3D),
                ProductColorOption("Deep Forest", 0xFF2B483D),
                ProductColorOption("Ink Black", 0xFF211F1E)
            ),
            sizes = listOf("One Size"),
            reviews = listOf(
                ProductReview("Samir K.", 5, "4 days ago", "Slimmer than my old wallet and smells amazing.")
            )
        )
    )

    fun findById(id: String): Product? = products.find { it.id == id }
}
