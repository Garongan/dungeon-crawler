import game.core.SceneNavigator
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.math.geom.*

/**
 * Entry point utama game Kedalaman Tak Berujung.
 *
 * Menginisialisasi KorGE engine dengan resolusi portrait pixel art (180×320),
 * mengonfigurasi SceneNavigator, dan menampilkan MainMenuScene.
 */
suspend fun main() = Korge(
    windowSize = Size(720, 1280),
    virtualSize = Size(180, 320),
    backgroundColor = Colors["#1a1a2e"]
) {
    views.gameWindow.fullscreen = true

    val sceneContainer = sceneContainer()

    // Inisialisasi SceneNavigator dengan referensi sceneContainer dan ukuran virtual
    SceneNavigator.init(
        sceneContainer = sceneContainer,
        virtualWidth = 180.0,
        virtualHeight = 320.0
    )

    // Navigasi ke scene pertama — MainMenuScene
    SceneNavigator.goToMainMenu()
}
