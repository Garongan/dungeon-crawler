package game.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed class yang merepresentasikan tingkat kelangkaan item.
 *
 * Setiap tingkatan memiliki [displayName] dalam Bahasa Indonesia
 * dan [colorHex] untuk pewarnaan UI tooltip dan badge.
 */
@Serializable
sealed class Rarity {

    /** Nama tampilan kelangkaan dalam Bahasa Indonesia */
    abstract val displayName: String

    /** Kode warna hex untuk pewarnaan UI */
    abstract val colorHex: String

    /** Biasa — item yang paling umum ditemukan */
    @Serializable
    @SerialName("common")
    data object Common : Rarity() {
        override val displayName: String = "Biasa"
        override val colorHex: String = "#AAAAAA"
    }

    /** Langka — item yang cukup jarang ditemukan */
    @Serializable
    @SerialName("rare")
    data object Rare : Rarity() {
        override val displayName: String = "Langka"
        override val colorHex: String = "#4A90D9"
    }

    /** Epik — item dengan kekuatan tinggi */
    @Serializable
    @SerialName("epic")
    data object Epic : Rarity() {
        override val displayName: String = "Epik"
        override val colorHex: String = "#A855F7"
    }

    /** Legendaris — item paling langka dan paling kuat */
    @Serializable
    @SerialName("legendary")
    data object Legendary : Rarity() {
        override val displayName: String = "Legendaris"
        override val colorHex: String = "#F59E0B"
    }
}
