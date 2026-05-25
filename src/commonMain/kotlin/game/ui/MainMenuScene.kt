package game.ui

import game.core.SceneNavigator
import korlibs.image.color.Colors
import korlibs.korge.input.onClickSuspend
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.view.align.*

/**
 * Scene menu utama yang ditampilkan saat game pertama kali dibuka.
 *
 * Menampilkan judul game "Kedalaman Tak Berujung" dengan tombol
 * untuk memulai petualangan (ke [HubScene]) atau keluar dari game.
 */
class MainMenuScene : Scene() {

    override suspend fun SContainer.sceneInit() {
        val root = this

        // Background gelap
        solidRect(root.width, root.height, Colors["#0A0A1E"])

        // Judul game — dipecah dua baris agar muat di resolusi 180px
        text("Kedalaman", textSize = 14.0, color = Colors["#E8D5B5"]) {
            centerXOn(root)
            y = 90.0
        }
        text("Tak Berujung", textSize = 14.0, color = Colors["#E8D5B5"]) {
            centerXOn(root)
            y = 108.0
        }

        // Subjudul genre
        text("Roguelike Dungeon Crawler", textSize = 6.0, color = Colors["#7A7A9A"]) {
            centerXOn(root)
            y = 130.0
        }

        // Tombol Mulai — navigasi ke HubScene
        val btnMulai = container {
            position((root.width - 80.0) / 2, 190.0)
            val bg = solidRect(80.0, 18.0, Colors["#3D3D5C"])
            text("Mulai", textSize = 8.0, color = Colors.WHITE) {
                centerOn(bg)
            }
        }
        btnMulai.onClickSuspend {
            SceneNavigator.goToHub()
        }

        // Tombol Keluar — menutup game
        val btnKeluar = container {
            position((root.width - 80.0) / 2, 215.0)
            val bg = solidRect(80.0, 18.0, Colors["#5C2020"])
            text("Keluar", textSize = 8.0, color = Colors["#FF9999"]) {
                centerOn(bg)
            }
        }
        btnKeluar.onClickSuspend {
            this@MainMenuScene.views.gameWindow.close()
        }
    }
}
