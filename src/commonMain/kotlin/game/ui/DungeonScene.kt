package game.ui

import game.core.*
import game.dungeon.ZoneTheme
import game.player.PlayerEntity
import korlibs.event.Key
import korlibs.image.color.Colors
import korlibs.korge.input.keys
import korlibs.korge.input.mouse
import korlibs.korge.input.onClickSuspend
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.time.*
import kotlin.math.*

/**
 * Scene utama gameplay dungeon.
 *
 * Mengelola layer rendering dan menampilkan dungeon sesuai
 * zona dan lantai yang diterima. Layer disusun dari bawah ke atas:
 * backgroundLayer → worldLayer → fxLayer → hudLayer → overlayLayer.
 *
 * Menyediakan pergerakan pemain via virtual joystick dan keyboard, serta
 * mekanisme dodge (hindar) dengan i-frame, visual flash, dan cooldown arc.
 *
 * @property zoneTheme Tema zona yang menentukan warna dan tileset
 * @property floorNumber Nomor lantai saat ini
 */
class DungeonScene(
    private val zoneTheme: ZoneTheme,
    private val floorNumber: Int
) : Scene() {

    /** Layer latar belakang (tilemap, dekorasi lantai) */
    private lateinit var backgroundLayer: Container

    /** Layer entitas game (player, enemy, projectile, loot) */
    private lateinit var worldLayer: Container

    /** Layer efek visual (partikel, floating damage) */
    private lateinit var fxLayer: Container

    /** Layer HUD (HP bar, minimap, skill cooldown) */
    private lateinit var hudLayer: Container

    /** Layer overlay (inventory, pause menu, event room UI) */
    private lateinit var overlayLayer: Container

    /** State logis pemain */
    private lateinit var player: PlayerEntity

    /** View visual pemain */
    private lateinit var playerView: Image

    /** Virtual joystick untuk kontrol gerakan */
    private lateinit var joystick: VirtualJoystick

    /** Akumulator waktu berjalan dalam milidetik untuk efek visual */
    private var elapsedMs: Double = 0.0

    override suspend fun SContainer.sceneInit() {
        val root = this

        // Background sesuai tema zona
        solidRect(root.width, root.height, Colors[zoneTheme.bgColorHex])

        // Inisialisasi layer sesuai urutan rendering (bottom → top)
        backgroundLayer = container { name = "backgroundLayer" }
        worldLayer = container { name = "worldLayer" }
        fxLayer = container { name = "fxLayer" }
        hudLayer = container { name = "hudLayer" }
        overlayLayer = container { name = "overlayLayer" }

        // HUD — info zona dan lantai saat ini
        hudLayer.apply {
            text(
                "${zoneTheme.displayName} — Lantai $floorNumber",
                textSize = 7.0,
                color = Colors.WHITE
            ) {
                position(4.0, 4.0)
            }
        }

        // 1. Inisialisasi Player State dan View
        player = PlayerEntity()
        player.x = root.width / 2.0
        player.y = root.height / 2.0

        val playerBitmap = ResourceLoader.loadBitmap(AssetPaths.Sprites.KNIGHT_IDLE)
        playerView = worldLayer.pixelImage(playerBitmap) {
            anchor(Anchor.CENTER)
            scale(1.5)
            position(player.x, player.y)
        }

        // 2. Inisialisasi Virtual Joystick di HUD (kiri bawah)
        joystick = VirtualJoystick()
        hudLayer.addChild(joystick)
        joystick.position(36.0, root.height - 36.0)

        // 3. Inisialisasi Tombol Dodge (Hindar) di HUD (kanan bawah)
        val dodgeButton = hudLayer.container {
            position(root.width - 28.0, root.height - 28.0)
        }

        val buttonBg = dodgeButton.circle(
            radius = 14.0f,
            fill = Colors["#4a0e8faa"],
            stroke = Colors["#e8e0c8aa"],
            strokeThickness = 1.5f
        ) {
            anchor(Anchor.CENTER)
        }

        dodgeButton.text("HINDAR", textSize = 4.5, color = Colors.WHITE) {
            centerOn(buttonBg)
        }

        val cooldownOverlay = dodgeButton.graphics {}

        // Visual hover/press pada tombol dodge (Aturan 05)
        dodgeButton.mouse {
            onOver {
                buttonBg.fill = Colors["#6c25c1aa"]
            }
            onOut {
                buttonBg.fill = Colors["#4a0e8faa"]
            }
            onDown {
                dodgeButton.scale = 0.95
            }
            onUp {
                dodgeButton.scale = 1.0
            }
            onUpOutside {
                dodgeButton.scale = 1.0
            }
        }

        // Trigger dodge saat tombol UI diklik
        dodgeButton.onClickSuspend {
            val currentDir = getInputDirection(joystick)
            player.startDodge(currentDir, this@DungeonScene)
        }

        // 4. Update Loop Game
        addUpdater { dt ->
            // Akumulasi waktu berjalan
            elapsedMs += dt.milliseconds

            // Ambil arah input gerakan saat ini
            val inputDir = getInputDirection(joystick)
            if (inputDir.length > 0f) {
                player.lastNonZeroDirection = inputDir
            }

            // Tentukan kecepatan gerak berdasarkan state dodge (Aturan 02)
            val currentSpeed = if (player.isDodging) {
                player.data.spd * GameConstants.SPEED_CONVERSION_FACTOR * GameConstants.DODGE_BURST_MULTIPLIER
            } else {
                player.data.spd * GameConstants.SPEED_CONVERSION_FACTOR
            }.toDouble()

            // Tentukan arah gerak aktual
            val moveDir = if (player.isDodging) player.dodgeDirection else inputDir

            // Terapkan pergerakan
            if (moveDir.length > 0f) {
                player.x += moveDir.x * currentSpeed * dt.seconds
                player.y += moveDir.y * currentSpeed * dt.seconds
            }

            // Batasi posisi player agar tetap di dalam layar
            val halfSize = 12.0
            player.x = player.x.coerceIn(halfSize, root.width - halfSize)
            player.y = player.y.coerceIn(halfSize, root.height - halfSize)

            playerView.position(player.x, player.y)

            // Update timer cooldown logic
            player.updateCooldown(dt.milliseconds.toLong())

            // Update visual circular arc cooldown overlay (Aturan 05)
            cooldownOverlay.updateShape {
                clear()
                if (player.dodgeCooldownRemainingMs > 0L) {
                    val ratio = player.dodgeCooldownRemainingMs.toDouble() / GameConstants.DODGE_COOLDOWN_MS.toDouble()
                    fill(Colors["#000000aa"]) {
                        moveTo(Point(0, 0))
                        val segments = 24
                        val radius = 14.5
                        val startAngle = -PI / 2.0
                        val endAngle = startAngle + ratio * 2.0 * PI
                        
                        lineTo(Point(cos(startAngle) * radius, sin(startAngle) * radius))
                        for (i in 0..segments) {
                            val t = i.toDouble() / segments
                            val angle = startAngle + t * (endAngle - startAngle)
                            lineTo(Point(cos(angle) * radius, sin(angle) * radius))
                          }
                          lineTo(Point(0, 0))
                          close()
                    }
                }
            }

            // Efek visual flash opacity selama i-frame (Aturan 05)
            if (player.isInvincible) {
                playerView.alpha = if ((elapsedMs / 50.0).toInt() % 2 == 0) 0.3 else 1.0
            } else {
                playerView.alpha = 1.0
            }

            // Trigger dodge via keyboard SPACE (kemudahan testing desktop)
            if (views.input.keys.justPressed(Key.SPACE)) {
                player.startDodge(inputDir, this@DungeonScene)
            }
        }

        // Overlay — tombol debug untuk testing navigasi (tetap dipertahankan)
        overlayLayer.apply {
            // Label debug
            text("[DEBUG]", textSize = 6.0, color = Colors["#FFFF00"]) {
                position(4.0, root.height - 60.0)
            }

            // Tombol debug: Simulasi Mati → GameOverScene
            val btnMati = container {
                position((root.width - 90.0) / 2, root.height - 48.0)
                val bg = solidRect(90.0, 16.0, Colors["#8B0000"])
                text("Simulasi Mati", textSize = 7.0, color = Colors["#FF6666"]) {
                    centerOn(bg)
                }
            }
            btnMati.onClickSuspend {
                SceneNavigator.goToGameOver(
                    floorReached = floorNumber,
                    darkSoulsEarned = floorNumber * 15
                )
            }

            // Tombol debug: Simulasi Menang → RunSummaryScene
            val btnMenang = container {
                position((root.width - 90.0) / 2, root.height - 28.0)
                val bg = solidRect(90.0, 16.0, Colors["#2D5A2D"])
                text("Simulasi Menang", textSize = 6.0, color = Colors["#AAFFAA"]) {
                    centerOn(bg)
                }
            }
            btnMenang.onClickSuspend {
                SceneNavigator.goToRunSummary(
                    floorReached = floorNumber,
                    enemiesKilled = floorNumber * 8,
                    itemsCollected = floorNumber * 3,
                    darkSoulsEarned = floorNumber * 25
                )
            }
        }
    }

    /**
     * Membaca input joystick dan keyboard untuk menghitung arah gerak pemain.
     */
    private fun getInputDirection(joystick: VirtualJoystick): Point {
        var dx = 0.0
        var dy = 0.0

        if (views.input.keys.pressing(Key.W) || views.input.keys.pressing(Key.UP)) dy -= 1.0
        if (views.input.keys.pressing(Key.S) || views.input.keys.pressing(Key.DOWN)) dy += 1.0
        if (views.input.keys.pressing(Key.A) || views.input.keys.pressing(Key.LEFT)) dx -= 1.0
        if (views.input.keys.pressing(Key.D) || views.input.keys.pressing(Key.RIGHT)) dx += 1.0

        val joyDir = joystick.direction
        if (joyDir.length > 0f) {
            dx += joyDir.x
            dy += joyDir.y
        }

        return if (dx == 0.0 && dy == 0.0) Point(0, 0) else Point(dx, dy).normalized
    }
}
