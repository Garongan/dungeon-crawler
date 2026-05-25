package game.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed class yang merepresentasikan jenis-jenis item secara polimorfik.
 *
 * Menggunakan sealed class agar `when` expression bersifat exhaustive
 * dan memungkinkan setiap subtipe membawa data tambahan di masa depan.
 */
@Serializable
sealed class ItemType {

    /** Senjata — digunakan untuk menyerang musuh */
    @Serializable
    @SerialName("weapon")
    data object Weapon : ItemType()

    /** Armor — digunakan untuk meningkatkan pertahanan */
    @Serializable
    @SerialName("armor")
    data object Armor : ItemType()

    /** Relic — item pasif yang memberikan efek unik */
    @Serializable
    @SerialName("relic")
    data object Relic : ItemType()

    /** Consumable — item sekali pakai (potion, scroll, dll.) */
    @Serializable
    @SerialName("consumable")
    data object Consumable : ItemType()
}
