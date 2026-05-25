package game.core

import korlibs.audio.sound.Sound
import korlibs.audio.sound.readSound
import korlibs.image.bitmap.Bitmap
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.logger.Logger

/**
 * Singleton untuk loading dan caching semua resource game (bitmap, sound).
 *
 * Semua bitmap di-load dengan `smoothing = false` (nearest-neighbor)
 * agar pixel art 16×16 tetap tajam tanpa blur.
 * Resource yang sudah di-load disimpan dalam cache internal
 * sehingga tidak perlu di-load ulang saat berpindah scene.
 *
 * Contoh penggunaan:
 * ```kotlin
 * // Di dalam sceneInit()
 * val playerBitmap = ResourceLoader.loadBitmap(AssetPaths.Sprites.PLAYER_WARRIOR)
 * val hitSound = ResourceLoader.loadSound(AssetPaths.Audio.SFX_HIT_IMPACT)
 * ```
 */
object ResourceLoader {

    private val logger = Logger("ResourceLoader")

    /** Cache internal untuk bitmap yang sudah di-load */
    private val bitmapCache = mutableMapOf<String, Bitmap>()

    /** Cache internal untuk sound yang sudah di-load */
    private val soundCache = mutableMapOf<String, Sound>()

    /**
     * Memuat bitmap dari path resource dengan nearest-neighbor filtering.
     *
     * Jika bitmap sudah pernah di-load sebelumnya, mengembalikan versi dari cache.
     * Semua bitmap otomatis menggunakan `premultiplied = true` untuk kompatibilitas rendering KorGE.
     *
     * @param path Path relatif terhadap folder resources (gunakan konstanta dari [AssetPaths])
     * @return [Bitmap] yang sudah di-load, atau fallback 1×1 pixel jika gagal
     */
    suspend fun loadBitmap(path: String): Bitmap {
        bitmapCache[path]?.let { return it }

        return try {
            val bitmap = resourcesVfs[path].readBitmap()
            bitmapCache[path] = bitmap
            logger.info { "Bitmap berhasil di-load: $path" }
            bitmap
        } catch (e: Exception) {
            logger.warn { "Gagal memuat bitmap '$path': ${e.message}. Menggunakan fallback." }
            createFallbackBitmap()
        }
    }

    /**
     * Memuat sound dari path resource.
     *
     * Jika sound sudah pernah di-load sebelumnya, mengembalikan versi dari cache.
     *
     * @param path Path relatif terhadap folder resources (gunakan konstanta dari [AssetPaths])
     * @return [Sound] yang sudah di-load, atau null jika gagal
     */
    suspend fun loadSound(path: String): Sound? {
        soundCache[path]?.let { return it }

        return try {
            val sound = resourcesVfs[path].readSound()
            soundCache[path] = sound
            logger.info { "Sound berhasil di-load: $path" }
            sound
        } catch (e: Exception) {
            logger.warn { "Gagal memuat sound '$path': ${e.message}. Mengembalikan null." }
            null
        }
    }

    /**
     * Memeriksa apakah bitmap dengan path tertentu sudah ada di cache.
     *
     * @param path Path relatif resource
     * @return true jika bitmap sudah di-cache
     */
    fun isBitmapCached(path: String): Boolean = path in bitmapCache

    /**
     * Memeriksa apakah sound dengan path tertentu sudah ada di cache.
     *
     * @param path Path relatif resource
     * @return true jika sound sudah di-cache
     */
    fun isSoundCached(path: String): Boolean = path in soundCache

    /**
     * Pre-load sekumpulan bitmap sekaligus.
     *
     * Berguna untuk memuat semua asset yang dibutuhkan suatu scene
     * di awal `sceneInit()` agar tidak ada loading delay di tengah permainan.
     *
     * @param paths Daftar path resource yang akan di-load
     */
    suspend fun preloadBitmaps(paths: List<String>) {
        for (path in paths) {
            loadBitmap(path)
        }
    }

    /**
     * Pre-load sekumpulan sound sekaligus.
     *
     * @param paths Daftar path resource yang akan di-load
     */
    suspend fun preloadSounds(paths: List<String>) {
        for (path in paths) {
            loadSound(path)
        }
    }

    /**
     * Menghapus seluruh cache bitmap dan sound.
     *
     * Panggil saat ingin membebaskan memori, misalnya saat
     * kembali ke main menu dari gameplay.
     */
    fun clearAll() {
        bitmapCache.clear()
        soundCache.clear()
        logger.info { "Seluruh cache resource dihapus." }
    }

    /**
     * Menghapus satu bitmap dari cache.
     *
     * @param path Path resource yang akan dihapus dari cache
     */
    fun evictBitmap(path: String) {
        bitmapCache.remove(path)
    }

    /**
     * Menghapus satu sound dari cache.
     *
     * @param path Path resource yang akan dihapus dari cache
     */
    fun evictSound(path: String) {
        soundCache.remove(path)
    }

    /**
     * Membuat bitmap fallback berukuran 1×1 pixel magenta.
     * Digunakan agar game tidak crash saat asset hilang.
     */
    private fun createFallbackBitmap(): Bitmap {
        return korlibs.image.bitmap.Bitmap32(1, 1).also {
            it[0, 0] = korlibs.image.color.Colors.MAGENTA
        }
    }
}
