package game.ui

import korlibs.image.color.Colors
import korlibs.korge.input.mouse
import korlibs.korge.view.*
import korlibs.math.geom.*
import game.core.GameConstants
import kotlin.math.*

/**
 * Komponen Virtual Joystick untuk mengontrol pergerakan pemain pada perangkat layar sentuh.
 *
 * Mengikuti Aturan 01 (Kotlin Code Style) dan Aturan 05 (UI & HUD Guidelines).
 * Menghasilkan input vector ternormalisasi agar kecepatan diagonal konsisten.
 */
class VirtualJoystick : Container() {

    private var _direction = Point(0f, 0f)

    /** Arah pergerakan ter-normalisasi (x, y) dengan magnitude 1.0 atau 0.0 */
    val direction: Point get() = _direction

    private var isDragging = false

    // Lingkaran luar (base)
    private val baseCircle = circle(
        radius = GameConstants.JOYSTICK_BASE_RADIUS,
        fill = Colors["#4a0e8f33"],
        stroke = Colors["#e8e0c8aa"],
        strokeThickness = 1.5f
    ) {
        anchor(Anchor.CENTER)
    }

    // Lingkaran dalam (knob/thumb)
    private val knobCircle = circle(
        radius = GameConstants.JOYSTICK_KNOB_RADIUS,
        fill = Colors["#e8e0c8"]
    ) {
        anchor(Anchor.CENTER)
    }

    init {
        // Posisikan base dan knob di tengah container joystick ini secara default
        baseCircle.position(0f, 0f)
        knobCircle.position(0f, 0f)

        // Daftarkan handler input mouse/sentuhan pada container joystick
        mouse {
            onDown { event ->
                isDragging = true
                updateJoystickState(event.currentPosLocal)
            }
            onMove { event ->
                if (isDragging) {
                    updateJoystickState(event.currentPosLocal)
                }
            }
            onUp {
                resetJoystickState()
            }
            onUpOutside {
                resetJoystickState()
            }
        }
    }

    /**
     * Memperbarui posisi knob joystick secara visual dan menghitung arah input yang ternormalisasi.
     *
     * @param localPos Posisi mouse/sentuhan dalam koordinat lokal joystick
     */
    private fun updateJoystickState(localPos: Point) {
        val distance = localPos.length.toDouble()
        if (distance == 0.0) {
            _direction = Point(0f, 0f)
            knobCircle.position(0f, 0f)
            return
        }

        // Arah vektor input
        val dirX = (localPos.x.toDouble() / distance).toFloat()
        val dirY = (localPos.y.toDouble() / distance).toFloat()

        // Batasi pergerakan knob agar tidak melebihi radius base
        val maxRadius = GameConstants.JOYSTICK_BASE_RADIUS.toDouble()
        val clampedDistance = min(distance, maxRadius).toFloat()

        knobCircle.position(dirX * clampedDistance, dirY * clampedDistance)

        // Normalisasi diagonal input agar kecepatan konsisten (magnitude = 1)
        _direction = Point(dirX, dirY)
    }

    /**
     * Mengembalikan knob ke posisi tengah dan mereset arah input menjadi nol.
     */
    private fun resetJoystickState() {
        isDragging = false
        _direction = Point(0f, 0f)
        knobCircle.position(0f, 0f)
    }
}
