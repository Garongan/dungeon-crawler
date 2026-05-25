package game.ui

import game.core.SceneNavigator
import korlibs.image.color.Colors
import korlibs.korge.input.onClickSuspend
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.view.align.*

/**
 * Scene yang ditampilkan saat pemain mati di dalam dungeon.
 *
 * Menampilkan teks "GAME OVER" dengan ringkasan singkat
 * berupa lantai yang dicapai dan Jiwa Kegelapan yang diperoleh.
 * Pemain dapat kembali ke [HubScene] untuk memulai run baru.
 *
 * @property floorReached Lantai terakhir yang berhasil dicapai
 * @property darkSoulsEarned Jumlah Jiwa Kegelapan yang diperoleh dalam run
 */
class GameOverScene(
    private val floorReached: Int,
    private val darkSoulsEarned: Int
) : Scene() {

    override suspend fun SContainer.sceneInit() {
        val root = this

        // Background gelap merah — nuansa kematian
        solidRect(root.width, root.height, Colors["#1A0A0A"])

        // Judul GAME OVER
        text("GAME OVER", textSize = 16.0, color = Colors["#CC3333"]) {
            centerXOn(root)
            y = 80.0
        }

        // Garis pemisah dekoratif
        solidRect(100.0, 1.0, Colors["#5C2020"]) {
            centerXOn(root)
            y = 110.0
        }

        // Statistik run
        text("Lantai Dicapai: $floorReached", textSize = 8.0, color = Colors["#CCCCCC"]) {
            centerXOn(root)
            y = 125.0
        }
        text("Jiwa Kegelapan: $darkSoulsEarned", textSize = 8.0, color = Colors["#AA88FF"]) {
            centerXOn(root)
            y = 142.0
        }

        // Tombol kembali ke lobby
        val btnBack = container {
            position((root.width - 100.0) / 2, 200.0)
            val bg = solidRect(100.0, 18.0, Colors["#3D3D5C"])
            text("Kembali ke Lobby", textSize = 7.0, color = Colors.WHITE) {
                centerOn(bg)
            }
        }
        btnBack.onClickSuspend {
            SceneNavigator.goToHub()
        }
    }
}
