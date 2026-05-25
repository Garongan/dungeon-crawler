package game.core

import korlibs.image.bitmap.Bitmap
import korlibs.korge.view.Container
import korlibs.korge.view.Image
import korlibs.korge.view.addTo
import korlibs.korge.view.image

/**
 * Extension function untuk membuat [Image] view dengan pixel art settings.
 *
 * Otomatis mengatur `smoothing = false` (nearest-neighbor) agar
 * sprite pixel art 16×16 tidak blur saat di-scale.
 * Gunakan fungsi ini sebagai pengganti `image()` standar KorGE
 * untuk semua tampilan pixel art.
 *
 * Contoh penggunaan:
 * ```kotlin
 * // Di dalam sceneMain() atau sceneInit()
 * val playerBitmap = ResourceLoader.loadBitmap(AssetPaths.Sprites.PLAYER_WARRIOR)
 * val playerImage = pixelImage(playerBitmap) {
 *     scale(4.0)
 *     position(100, 100)
 * }
 * ```
 *
 * @param bitmap Bitmap yang akan ditampilkan
 * @param block Lambda opsional untuk konfigurasi tambahan pada Image view
 * @return [Image] view yang sudah dikonfigurasi untuk pixel art
 */
fun Container.pixelImage(
    bitmap: Bitmap,
    block: Image.() -> Unit = {}
): Image {
    return image(bitmap) {
        smoothing = false
        block()
    }
}
