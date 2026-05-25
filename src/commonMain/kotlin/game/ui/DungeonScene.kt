package game.ui

import game.core.SceneNavigator
import game.dungeon.ZoneTheme
import korlibs.image.color.Colors
import korlibs.korge.input.onClickSuspend
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.view.align.*

/**
 * Scene utama gameplay dungeon.
 *
 * Mengelola layer rendering dan menampilkan dungeon sesuai
 * zona dan lantai yang diterima. Layer disusun dari bawah ke atas:
 * backgroundLayer → worldLayer → fxLayer → hudLayer → overlayLayer.
 *
 * Saat ini menyediakan tombol debug untuk menguji navigasi
 * ke [GameOverScene] dan [RunSummaryScene].
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

        // Overlay — tombol debug untuk testing navigasi
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
}
