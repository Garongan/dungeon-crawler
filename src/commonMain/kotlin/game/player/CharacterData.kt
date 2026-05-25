package game.player

import kotlinx.serialization.Serializable

/**
 * Data karakter pemain yang menyimpan seluruh statistik dasar.
 *
 * Digunakan sebagai kontainer data untuk merepresentasikan
 * status karakter dalam satu sesi permainan (run).
 *
 * @property name Nama karakter pemain
 * @property hp Hit point saat ini
 * @property maxHp Hit point maksimum
 * @property atk Kekuatan serangan dasar
 * @property def Pertahanan dasar
 * @property spd Kecepatan gerak karakter
 * @property mana Mana saat ini
 * @property maxMana Mana maksimum
 */
@Serializable
data class CharacterData(
    val name: String,
    val hp: Int,
    val maxHp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val mana: Int,
    val maxMana: Int
) {
    /** Apakah karakter masih hidup */
    val isAlive: Boolean get() = hp > 0

    /** Apakah mana penuh */
    val hasFullMana: Boolean get() = mana >= maxMana

    companion object {
        /**
         * Membuat CharacterData default untuk keperluan fallback
         * ketika data save rusak atau tidak tersedia.
         */
        fun default(): CharacterData = CharacterData(
            name = "Pahlawan",
            hp = 100,
            maxHp = 100,
            atk = 10,
            def = 5,
            spd = 3,
            mana = 50,
            maxMana = 50
        )
    }
}
