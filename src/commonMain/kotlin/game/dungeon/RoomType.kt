package game.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed class yang merepresentasikan jenis-jenis ruangan dungeon.
 *
 * Menggunakan sealed class agar setiap tipe ruangan dapat membawa
 * properti konfigurasi spesifik dan mendukung `when` expression exhaustive.
 */
@Serializable
sealed class RoomType {

    /** Ruangan awal tempat pemain muncul di lantai baru */
    @Serializable
    @SerialName("spawn")
    data object Spawn : RoomType()

    /**
     * Ruangan pertarungan dengan gelombang musuh.
     *
     * @property waveCount Jumlah gelombang musuh dalam ruangan ini
     */
    @Serializable
    @SerialName("combat")
    data class Combat(val waveCount: Int = 1) : RoomType()

    /** Ruangan harta karun berisi peti jarahan */
    @Serializable
    @SerialName("treasure")
    data object Treasure : RoomType()

    /** Ruangan toko untuk membeli atau menjual item */
    @Serializable
    @SerialName("shop")
    data object Shop : RoomType()

    /** Ruangan bos — pertarungan dengan musuh bos lantai */
    @Serializable
    @SerialName("boss")
    data object Boss : RoomType()

    /** Ruangan istirahat untuk memulihkan HP atau meng-upgrade */
    @Serializable
    @SerialName("rest")
    data object Rest : RoomType()
}
