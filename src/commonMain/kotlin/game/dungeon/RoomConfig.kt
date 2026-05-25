package game.dungeon

import kotlinx.serialization.Serializable

/**
 * Konfigurasi sebuah ruangan dalam dungeon.
 *
 * Digunakan oleh FloorGenerator untuk menentukan jenis ruangan,
 * bobot kemunculan musuh, dan tabel jarahan yang tersedia.
 *
 * @property type Jenis ruangan (Spawn, Combat, Treasure, Shop, Boss, Rest)
 * @property enemyWeights Bobot kemunculan musuh berdasarkan ID musuh.
 *   Key = ID musuh, Value = bobot probabilitas relatif.
 *   Kosong untuk ruangan non-combat.
 * @property lootTable Konfigurasi jarahan yang tersedia di ruangan ini
 */
@Serializable
data class RoomConfig(
    val type: RoomType,
    val enemyWeights: Map<String, Double> = emptyMap(),
    val lootTable: LootTable = LootTable.Empty
)
