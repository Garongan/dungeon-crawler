package game.items

import kotlinx.serialization.Serializable

/**
 * Data item/perlengkapan yang menyimpan seluruh informasi tentang sebuah item.
 *
 * Digunakan sebagai kontainer data yang dimuat dari JSON (`resources/data/items.json`)
 * untuk mendukung desain berbasis data (data-driven design).
 *
 * @property id Identifier unik item (contoh: "sword_rusty", "relic_vampiric")
 * @property name Nama tampilan item dalam Bahasa Indonesia
 * @property type Jenis item — Weapon, Armor, Relic, atau Consumable
 * @property rarity Tingkat kelangkaan item
 * @property statsBonus Bonus statistik yang diberikan item saat digunakan
 * @property passive Efek pasif dari item
 * @property description Deskripsi singkat item untuk tooltip UI
 */
@Serializable
data class ItemData(
    val id: String,
    val name: String,
    val type: ItemType,
    val rarity: Rarity,
    val statsBonus: StatsBonus = StatsBonus.NONE,
    val passive: PassiveEffect = PassiveEffect.None,
    val description: String = ""
)
