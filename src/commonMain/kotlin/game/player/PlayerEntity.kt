package game.player

import game.core.GameConstants
import korlibs.math.geom.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Representasi state logis dari karakter pemain (player).
 *
 * Menyimpan data statistik dasar ([CharacterData]) serta status
 * dinamis game seperti posisi koordinat, status kekebalan (invincibility),
 * dan kemampuan menghindar (dodge) beserta cooldown-nya.
 *
 * Mengikuti Aturan 01 (Kotlin Code Style) dan Aturan 03 (Game Architecture).
 *
 * @property data Statistik dasar karakter pemain
 */
class PlayerEntity(val data: CharacterData = CharacterData.default()) {

    /** Posisi X pemain di koordinat dunia */
    var x: Double = 0.0

    /** Posisi Y pemain di koordinat dunia */
    var y: Double = 0.0

    /** Status apakah pemain kebal terhadap serangan saat ini (i-frame aktif) */
    var isInvincible: Boolean = false
        private set

    /** Apakah pemain saat ini diperbolehkan untuk melakukan dodge */
    var canDodge: Boolean = true
        private set

    /** Apakah pemain saat ini sedang berada dalam animasi/durasi dodge */
    var isDodging: Boolean = false
        private set

    /** Vektor arah gerak saat dodge dipicu */
    var dodgeDirection: Point = Point(0, 0)
        private set

    /** Arah pergerakan non-zero terakhir untuk menentukan arah hadap / arah dodge default */
    var lastNonZeroDirection: Point = Point(1, 0)

    /** Waktu tersisa untuk cooldown dodge dalam milidetik */
    var dodgeCooldownRemainingMs: Long = 0L

    /**
     * Memulai aksi menghindar (dodge).
     *
     * Ketika dipicu, status [isInvincible] dan [isDodging] diset menjadi true,
     * dan kecepatan burst diterapkan ke arah pergerakan saat ini (atau arah terakhir).
     * Meluncurkan coroutine untuk menangani durasi dodge dan masa cooldown setelahnya.
     *
     * @param movementDir Arah pergerakan input saat ini (dari joystick/keyboard)
     * @param scope Scope coroutine scene untuk meluncurkan timer delay
     */
    fun startDodge(movementDir: Point, scope: CoroutineScope) {
        if (!canDodge || isDodging) return

        isDodging = true
        isInvincible = true
        
        // Gunakan arah movement jika ada, jika tidak gunakan arah non-zero terakhir
        val finalDir = if (movementDir.length > 0f) movementDir.normalized else lastNonZeroDirection
        dodgeDirection = finalDir

        scope.launch {
            // Durasi dodge (i-frame) selama 300ms
            delay(GameConstants.DODGE_DURATION_MS)
            
            isInvincible = false
            isDodging = false
            canDodge = false

            // Set waktu cooldown awal untuk pelacakan visual
            dodgeCooldownRemainingMs = GameConstants.DODGE_COOLDOWN_MS

            // Durasi cooldown selama 1500ms
            delay(GameConstants.DODGE_COOLDOWN_MS)
            
            canDodge = true
        }
    }

    /**
     * Memperbarui sisa waktu cooldown dodge setiap frame.
     *
     * @param dtMs Waktu delta dalam milidetik sejak frame terakhir
     */
    fun updateCooldown(dtMs: Long) {
        if (dodgeCooldownRemainingMs > 0) {
            dodgeCooldownRemainingMs = (dodgeCooldownRemainingMs - dtMs).coerceAtLeast(0L)
        }
    }
}
