package game.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed class yang merepresentasikan efek pasif dari item secara polimorfik.
 *
 * Setiap subkelas dapat membawa parameter konfigurasi khusus,
 * sehingga efek pasif dapat dikustomisasi melalui data JSON.
 */
@Serializable
sealed class PassiveEffect {

    /** Tidak ada efek pasif */
    @Serializable
    @SerialName("none")
    data object None : PassiveEffect()

    /**
     * Mencuri nyawa saat menyerang musuh.
     *
     * @property percentage Persentase damage yang dikonversi menjadi HP (0.0 - 1.0)
     */
    @Serializable
    @SerialName("lifesteal")
    data class Lifesteal(val percentage: Double) : PassiveEffect()

    /**
     * Memberikan efek terbakar pada musuh saat menyerang.
     *
     * @property damagePerTick Damage per tick dari efek terbakar
     * @property durationMs Durasi total efek terbakar dalam milidetik
     */
    @Serializable
    @SerialName("burn_on_hit")
    data class BurnOnHit(
        val damagePerTick: Int,
        val durationMs: Long
    ) : PassiveEffect()

    /**
     * Meningkatkan kecepatan gerak pemain.
     *
     * @property multiplier Pengali kecepatan (contoh: 1.3 = +30% kecepatan)
     */
    @Serializable
    @SerialName("speed_boost")
    data class SpeedBoost(val multiplier: Double) : PassiveEffect()
}
