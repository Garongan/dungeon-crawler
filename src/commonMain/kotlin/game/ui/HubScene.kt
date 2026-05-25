package game.ui

import game.core.SceneNavigator
import game.dungeon.ZoneTheme
import korlibs.image.color.Colors
import korlibs.korge.input.onClickSuspend
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.view.align.*

/**
 * Scene lobby (hub) tempat pemain bersiap sebelum masuk dungeon.
 *
 * Dari sini pemain dapat memilih untuk masuk ke dungeon
 * atau kembali ke menu utama. Di iterasi berikutnya, scene ini
 * akan menampilkan upgrade tree, toko, dan pilihan karakter.
 */
class HubScene : Scene() {

    override suspend fun SContainer.sceneInit() {
        val root = this

        // Background lobby — nuansa gelap hangat
        solidRect(root.width, root.height, Colors["#1E1E3A"])

        // Judul Lobby
        text("Lobby", textSize = 12.0, color = Colors["#C8B882"]) {
            centerXOn(root)
            y = 40.0
        }

        // Deskripsi singkat
        text("Bersiap untuk petualangan", textSize = 6.0, color = Colors["#8888AA"]) {
            centerXOn(root)
            y = 60.0
        }

        // Garis pemisah dekoratif
        solidRect(120.0, 1.0, Colors["#3D3D5C"]) {
            centerXOn(root)
            y = 75.0
        }

        // Tombol Masuk Dungeon — masuk ke zona pertama (Reruntuhan, lantai 1)
        val btnDungeon = container {
            position((root.width - 100.0) / 2, 150.0)
            val bg = solidRect(100.0, 18.0, Colors["#2D5A2D"])
            text("Masuk Dungeon", textSize = 8.0, color = Colors["#AAFFAA"]) {
                centerOn(bg)
            }
        }
        btnDungeon.onClickSuspend {
            SceneNavigator.goToDungeon(ZoneTheme.RERUNTUHAN, 1)
        }

        // Tombol Kembali ke Menu Utama
        val btnBack = container {
            position((root.width - 100.0) / 2, 175.0)
            val bg = solidRect(100.0, 18.0, Colors["#3D3D5C"])
            text("Kembali ke Menu", textSize = 7.0, color = Colors["#CCCCEE"]) {
                centerOn(bg)
            }
        }
        btnBack.onClickSuspend {
            SceneNavigator.goToMainMenu()
        }
    }
}
