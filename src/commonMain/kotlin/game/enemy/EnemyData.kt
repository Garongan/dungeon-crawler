package game.enemy

import game.dungeon.ZoneTheme
import kotlinx.serialization.Serializable

/**
 * Data musuh yang menyimpan statistik dasar dan konfigurasi perilaku.
 *
 * Dimuat dari JSON (`resources/data/enemies.json`) untuk mendukung
 * desain berbasis data. Instansiasi musuh di-runtime melalui EnemyFactory.
 *
 * @property name Nama musuh dalam Bahasa Indonesia (contoh: "Slime Beracun")
 * @property hp Hit point dasar musuh
 * @property atk Kekuatan serangan dasar musuh
 * @property behavior Tipe perilaku AI musuh (Patrol, Aggressive, Ranged, BossBehavior)
 * @property zone Zona dungeon tempat musuh ini muncul
 */
@Serializable
data class EnemyData(
    val name: String,
    val hp: Int,
    val atk: Int,
    val behavior: BehaviorType,
    val zone: ZoneTheme
)
