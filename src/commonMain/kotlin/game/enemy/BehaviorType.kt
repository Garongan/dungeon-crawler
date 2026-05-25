package game.enemy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed class yang merepresentasikan tipe perilaku musuh secara polimorfik.
 *
 * Setiap subtipe dapat membawa parameter konfigurasi spesifik
 * yang memengaruhi cara musuh bergerak dan menyerang.
 */
@Serializable
sealed class BehaviorType {

    /**
     * Musuh berpatroli di area tertentu dan menyerang jika pemain mendekat.
     *
     * @property patrolRadius Radius patroli dalam satuan tile
     */
    @Serializable
    @SerialName("patrol")
    data class Patrol(val patrolRadius: Int = 3) : BehaviorType()

    /**
     * Musuh langsung mengejar dan menyerang pemain secara agresif.
     *
     * @property chaseSpeed Kecepatan mengejar relatif terhadap kecepatan dasar musuh
     */
    @Serializable
    @SerialName("aggressive")
    data class Aggressive(val chaseSpeed: Double = 1.0) : BehaviorType()

    /**
     * Musuh menyerang dari jarak jauh dengan proyektil.
     *
     * @property attackRange Jarak tembak dalam satuan tile
     * @property projectileSpeed Kecepatan proyektil
     */
    @Serializable
    @SerialName("ranged")
    data class Ranged(
        val attackRange: Int = 5,
        val projectileSpeed: Double = 2.0
    ) : BehaviorType()

    /**
     * Perilaku khusus bos dengan pola fase bertahap.
     *
     * @property phaseCount Jumlah fase pertarungan bos
     */
    @Serializable
    @SerialName("boss")
    data class BossBehavior(val phaseCount: Int = 2) : BehaviorType()
}
