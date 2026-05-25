package game.items

import kotlinx.serialization.Serializable

/**
 * Bonus statistik yang diberikan oleh sebuah item.
 *
 * Semua nilai default ke 0, sehingga item hanya perlu menentukan
 * bonus yang relevan tanpa harus mengisi semua field.
 *
 * @property hp Bonus hit point
 * @property atk Bonus kekuatan serangan
 * @property def Bonus pertahanan
 * @property spd Bonus kecepatan
 * @property mana Bonus mana
 */
@Serializable
data class StatsBonus(
    val hp: Int = 0,
    val atk: Int = 0,
    val def: Int = 0,
    val spd: Int = 0,
    val mana: Int = 0
) {
    /** Apakah bonus ini kosong (semua nilai nol) */
    val isEmpty: Boolean get() = hp == 0 && atk == 0 && def == 0 && spd == 0 && mana == 0

    companion object {
        /** StatsBonus kosong tanpa bonus apapun */
        val NONE: StatsBonus = StatsBonus()
    }
}
