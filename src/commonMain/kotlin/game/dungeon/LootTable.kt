package game.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed class yang merepresentasikan tabel jarahan (loot table) ruangan.
 *
 * Menggunakan sealed class agar konfigurasi jarahan dapat bervariasi
 * secara polimorfik — dari ruangan tanpa jarahan hingga jarahan acak
 * dengan bobot probabilitas.
 */
@Serializable
sealed class LootTable {

    /** Tidak ada jarahan sama sekali */
    @Serializable
    @SerialName("empty")
    data object Empty : LootTable()

    /**
     * Jarahan yang dijamin muncul (guaranteed drop).
     *
     * @property itemIds Daftar ID item yang pasti didapatkan pemain
     */
    @Serializable
    @SerialName("guaranteed")
    data class Guaranteed(val itemIds: List<String>) : LootTable()

    /**
     * Jarahan acak berdasarkan bobot probabilitas.
     *
     * @property entries Daftar entri jarahan dengan ID item dan bobotnya
     * @property maxDrops Jumlah maksimum item yang bisa di-drop
     */
    @Serializable
    @SerialName("random")
    data class RandomLoot(
        val entries: List<LootEntry>,
        val maxDrops: Int = 1
    ) : LootTable()
}

/**
 * Entri tunggal dalam tabel jarahan acak.
 *
 * @property itemId ID item yang berpotensi di-drop
 * @property weight Bobot probabilitas relatif terhadap entri lain
 */
@Serializable
data class LootEntry(
    val itemId: String,
    val weight: Double
)
