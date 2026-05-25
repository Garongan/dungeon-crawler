package game.core

/**
 * Konstanta global yang mengatur gameplay dan konfigurasi engine.
 *
 * Semua konstanta numerik, terutama nilai balance dan dimensi UI global,
 * didefinisikan di sini sesuai Aturan 01 (Kotlin Code Style) dan Aturan 03 (Game Architecture).
 */
object GameConstants {

    /** Radius lingkaran luar (base) dari virtual joystick dalam pixel */
    const val JOYSTICK_BASE_RADIUS = 24.0f

    /** Radius lingkaran dalam (knob/thumb) dari virtual joystick dalam pixel */
    const val JOYSTICK_KNOB_RADIUS = 8.0f

    /**
     * Faktor konversi dari unit statistik kecepatan gerak pemain (CharacterData.spd)
     * ke piksel per detik. Nilai ini dikalikan dengan statistik spd karakter
     * untuk menentukan kecepatan pergerakan aktual di layar.
     */
    const val SPEED_CONVERSION_FACTOR = 40.0f
}
