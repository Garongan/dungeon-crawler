package game.core

import game.dungeon.ZoneTheme
import game.ui.DungeonScene
import game.ui.GameOverScene
import game.ui.HubScene
import game.ui.MainMenuScene
import game.ui.RunSummaryScene
import korlibs.image.color.Colors
import korlibs.korge.scene.SceneContainer
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.logger.Logger
import korlibs.math.interpolation.*
import korlibs.time.milliseconds

/**
 * Singleton untuk mengelola navigasi antar scene dalam game.
 *
 * Menyediakan fungsi navigasi type-safe ke setiap scene
 * dengan efek transisi black fade (300ms fade out, ganti scene, 300ms fade in).
 * Harus diinisialisasi sekali melalui [init] sebelum digunakan.
 *
 * Contoh penggunaan:
 * ```kotlin
 * // Di main()
 * SceneNavigator.init(sceneContainer, 180.0, 320.0)
 * SceneNavigator.goToMainMenu()
 *
 * // Di dalam scene
 * SceneNavigator.goToDungeon(ZoneTheme.RERUNTUHAN, 1)
 * ```
 */
object SceneNavigator {

    private val logger = Logger("SceneNavigator")

    /** Referensi ke SceneContainer dari KorGE */
    private lateinit var sceneContainer: SceneContainer

    /** Lebar virtual layar untuk overlay fade */
    private var virtualWidth = 180.0

    /** Tinggi virtual layar untuk overlay fade */
    private var virtualHeight = 320.0

    // Durasi fade masuk/keluar dalam milidetik
    private const val FADE_DURATION_MS = 300L

    /** Flag apakah transisi sedang berjalan, untuk mencegah navigasi ganda */
    private var isTransitioning = false

    /**
     * Menginisialisasi SceneNavigator dengan referensi yang dibutuhkan.
     *
     * Harus dipanggil sekali dari main() sebelum navigasi pertama.
     *
     * @param sceneContainer SceneContainer dari KorGE untuk manajemen scene
     * @param virtualWidth Lebar virtual layar untuk ukuran overlay fade
     * @param virtualHeight Tinggi virtual layar untuk ukuran overlay fade
     */
    fun init(
        sceneContainer: SceneContainer,
        virtualWidth: Double,
        virtualHeight: Double
    ) {
        this.sceneContainer = sceneContainer
        this.virtualWidth = virtualWidth
        this.virtualHeight = virtualHeight
        logger.info { "SceneNavigator terinisialisasi (${virtualWidth}x${virtualHeight})." }
    }

    /**
     * Melakukan transisi scene dengan efek black fade.
     *
     * Urutan: fade out 300ms → ganti scene → fade in 300ms.
     * Jika transisi sedang berjalan, navigasi baru diabaikan
     * untuk mencegah race condition.
     *
     * @param changeScene Lambda suspend yang melakukan perpindahan scene
     */
    private suspend fun fadeTransition(changeScene: suspend () -> Unit) {
        if (isTransitioning) {
            logger.warn { "Transisi sedang berjalan, navigasi diabaikan." }
            return
        }
        isTransitioning = true

        try {
            val parent = sceneContainer.parent
            if (parent == null) {
                logger.warn { "Parent container null, transisi tanpa fade." }
                changeScene()
                return
            }

            // Overlay hitam fullscreen untuk efek fade
            val overlay = parent.solidRect(virtualWidth, virtualHeight, Colors.BLACK) {
                alpha = 0.0
            }

            // Fade out — layar menjadi gelap
            overlay.tween(
                overlay::alpha[1.0],
                time = FADE_DURATION_MS.milliseconds,
                easing = Easing.EASE_IN_OUT
            )

            // Ganti scene saat layar gelap
            changeScene()

            // Fade in — layar kembali terang
            overlay.tween(
                overlay::alpha[0.0],
                time = FADE_DURATION_MS.milliseconds,
                easing = Easing.EASE_IN_OUT
            )

            overlay.removeFromParent()
        } finally {
            isTransitioning = false
        }
    }

    /** Navigasi ke [MainMenuScene] */
    suspend fun goToMainMenu() {
        logger.info { "Navigasi ke MainMenuScene" }
        fadeTransition {
            sceneContainer.changeTo { MainMenuScene() }
        }
    }

    /** Navigasi ke [HubScene] (Lobby) */
    suspend fun goToHub() {
        logger.info { "Navigasi ke HubScene" }
        fadeTransition {
            sceneContainer.changeTo { HubScene() }
        }
    }

    /**
     * Navigasi ke [DungeonScene] dengan zona dan lantai tertentu.
     *
     * @param zoneTheme Tema zona dungeon yang akan dimainkan
     * @param floorNumber Nomor lantai awal
     */
    suspend fun goToDungeon(zoneTheme: ZoneTheme, floorNumber: Int) {
        logger.info { "Navigasi ke DungeonScene (${zoneTheme.displayName}, lantai $floorNumber)" }
        fadeTransition {
            sceneContainer.changeTo { DungeonScene(zoneTheme, floorNumber) }
        }
    }

    /**
     * Navigasi ke [GameOverScene] saat player mati.
     *
     * @param floorReached Lantai terakhir yang dicapai
     * @param darkSoulsEarned Jumlah Jiwa Kegelapan yang diperoleh dalam run ini
     */
    suspend fun goToGameOver(floorReached: Int, darkSoulsEarned: Int) {
        logger.info { "Navigasi ke GameOverScene (lantai $floorReached, jiwa $darkSoulsEarned)" }
        fadeTransition {
            sceneContainer.changeTo { GameOverScene(floorReached, darkSoulsEarned) }
        }
    }

    /**
     * Navigasi ke [RunSummaryScene] setelah run selesai.
     *
     * @param floorReached Lantai terakhir yang dicapai
     * @param enemiesKilled Jumlah musuh yang dikalahkan
     * @param itemsCollected Jumlah item yang dikumpulkan
     * @param darkSoulsEarned Jumlah Jiwa Kegelapan yang diperoleh
     */
    suspend fun goToRunSummary(
        floorReached: Int,
        enemiesKilled: Int,
        itemsCollected: Int,
        darkSoulsEarned: Int
    ) {
        logger.info { "Navigasi ke RunSummaryScene" }
        fadeTransition {
            sceneContainer.changeTo {
                RunSummaryScene(floorReached, enemiesKilled, itemsCollected, darkSoulsEarned)
            }
        }
    }
}
