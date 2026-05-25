package game.ui

import game.core.SceneNavigator
import korlibs.image.color.Colors
import korlibs.korge.input.onClickSuspend
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.korge.view.align.*

/**
 * Scene ringkasan yang ditampilkan setelah run dungeon selesai (menang).
 *
 * Menampilkan statistik lengkap dari run yang baru diselesaikan:
 * lantai yang dicapai, musuh yang dikalahkan, item yang dikumpulkan,
 * dan Jiwa Kegelapan yang diperoleh.
 *
 * @property floorReached Lantai terakhir yang dicapai
 * @property enemiesKilled Jumlah musuh yang berhasil dikalahkan
 * @property itemsCollected Jumlah item yang dikumpulkan selama run
 * @property darkSoulsEarned Jumlah Jiwa Kegelapan yang diperoleh
 */
class RunSummaryScene(
    private val floorReached: Int,
    private val enemiesKilled: Int,
    private val itemsCollected: Int,
    private val darkSoulsEarned: Int
) : Scene() {

    override suspend fun SContainer.sceneInit() {
        val root = this

        // Background gelap keemasan — nuansa kemenangan
        solidRect(root.width, root.height, Colors["#1A1A0A"])

        // Judul — dipecah dua baris agar muat
        text("Ringkasan", textSize = 12.0, color = Colors["#FFD700"]) {
            centerXOn(root)
            y = 40.0
        }
        text("Petualangan", textSize = 12.0, color = Colors["#FFD700"]) {
            centerXOn(root)
            y = 56.0
        }

        // Garis pemisah dekoratif
        solidRect(100.0, 1.0, Colors["#5A5A2D"]) {
            centerXOn(root)
            y = 78.0
        }

        // Statistik run — setiap baris dengan warna tematik
        val statsStartY = 90.0
        // Jarak antar baris statistik dalam pixel virtual
        val lineHeight = 16.0

        text("Lantai Dicapai: $floorReached", textSize = 8.0, color = Colors["#CCCCCC"]) {
            centerXOn(root)
            y = statsStartY
        }
        text("Musuh Dikalahkan: $enemiesKilled", textSize = 8.0, color = Colors["#FF8888"]) {
            centerXOn(root)
            y = statsStartY + lineHeight
        }
        text("Item Terkumpul: $itemsCollected", textSize = 8.0, color = Colors["#88FF88"]) {
            centerXOn(root)
            y = statsStartY + lineHeight * 2
        }
        text("Jiwa Kegelapan: $darkSoulsEarned", textSize = 8.0, color = Colors["#AA88FF"]) {
            centerXOn(root)
            y = statsStartY + lineHeight * 3
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
