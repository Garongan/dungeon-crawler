package game.dungeon

import kotlinx.serialization.Serializable

/**
 * Enum class yang merepresentasikan tema zona dungeon.
 *
 * Setiap zona memiliki palette warna unik sesuai Aturan 04 (Assets & Audio).
 * Properti [tilesetPath], [bgColorHex], dan [ambientTintHex]
 * digunakan oleh DungeonScene untuk menerapkan visual zona.
 *
 * @property displayName Nama zona dalam Bahasa Indonesia
 * @property tilesetPath Path ke tileset zona di resources
 * @property bgColorHex Warna latar belakang zona (format hex)
 * @property ambientTintHex Warna tint ambient zona (format hex)
 */
@Serializable
enum class ZoneTheme(
    val displayName: String,
    val tilesetPath: String,
    val bgColorHex: String,
    val ambientTintHex: String
) {
    /** Zona 1 — Reruntuhan kuno dengan nuansa cokelat dan batu */
    RERUNTUHAN(
        displayName = "Reruntuhan",
        tilesetPath = "tiles/zone1_reruntuhan.png",
        bgColorHex = "#5C3A1E",
        ambientTintHex = "#FF8C00"
    ),

    /** Zona 2 — Katakombe gelap dengan nuansa ungu dan tulang */
    KATAKOMBE(
        displayName = "Katakombe",
        tilesetPath = "tiles/zone2_katakombe.png",
        bgColorHex = "#1A1A2E",
        ambientTintHex = "#4A0E8F"
    ),

    /** Zona 3 — Gua Api dengan lava dan oranye menyala */
    GUA_API(
        displayName = "Gua Api",
        tilesetPath = "tiles/zone3_gua_api.png",
        bgColorHex = "#8B0000",
        ambientTintHex = "#FF4500"
    ),

    /** Zona 4 — Inti Kegelapan, zona terakhir yang paling gelap */
    INTI_KEGELAPAN(
        displayName = "Inti Kegelapan",
        tilesetPath = "tiles/zone4_inti_kegelapan.png",
        bgColorHex = "#0A0A0A",
        ambientTintHex = "#0D1B4B"
    )
}
