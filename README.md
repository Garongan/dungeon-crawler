# Kedalaman Tak Berujung (Endless Depth) — Dungeon Crawler

**Kedalaman Tak Berujung** adalah game *rogue-lite dungeon crawler* pixel art 2D yang dibangun menggunakan game engine **KorGE** dan **Kotlin Multiplatform**. Telusuri reruntuhan kuno, hadapi monster legendaris, kumpulkan relik berkekuatan mistis, dan kumpulkan jiwa kegelapan (*dark souls*) untuk memperkuat dirimu dalam petualangan tiada akhir.

---

## 🎮 Fitur Utama / Key Features

* **Multiplatform**: Dapat dijalankan di Desktop (JVM & Native), Web (JS & WebAssembly), Android, dan iOS.
* **Pixel Art Estetis**: Menggunakan grid pixel art 16×16 yang tajam dengan 4 zona bertema unik.
* **Pertempuran Dinamis**: Sistem combat berbasis status efek, *dodge mechanics* dengan i-frame, dan perhitungan damage terpusat.
* **Data-Driven**: Seluruh balance game (HP, ATK, drop rates, item) dikonfigurasi melalui file JSON eksternal agar mudah diubah.
* **Sistem Upgrade & Save**: Simpan progres permainan dan perkuat karakter menggunakan *upgrade tree* di Hub.

---

## 🛠️ Struktur Proyek / Project Package Structure

Proyek ini terorganisasi dengan struktur package yang ketat guna memisahkan logika permainan dari visual rendering:

```text
src/commonMain/kotlin/game/
├── core/           # GameConstants, AssetPaths, ResourceCache, EventBus
├── player/         # PlayerEntity, PlayerStats, ActiveSkill subclasses
├── enemy/          # EnemyBase, EnemyFactory, BossBase, enemy subclasses
├── dungeon/        # FloorGenerator (BSP generator), RoomDefinition, DoorView, MinimapView
├── combat/         # DamageCalculator, StatusEffectManager, ProjectileBase
├── items/          # ItemData, ItemDatabase, RelicEffect, InventoryManager
├── ui/             # GameHUD, InventoryPanel, BossHealthBar, FloatingText
├── meta/           # SaveManager, UpgradeTree, AchievementManager, DarkSoulManager
└── audio/          # MusicManager, SoundManager
```

* **Data-driven resources** berada di `src/commonMain/resources/data/` (JSON) dan aset grafis/audio di subfolder resources yang sesuai.

---

## 🚀 Cara Menjalankan & Membangun / How to Run & Build

Proyek ini dikelola menggunakan Gradle Kotlin DSL. Pastikan Anda telah memasang JDK 11 atau yang lebih baru.

### Menjalankan Mode Pengembangan (Development Run)

#### 🔥 Hot Reload (Rekomendasi Utama saat Dev)
Kami telah menyediakan runner khusus yang secara otomatis mendeteksi **Java 21** pada macOS Anda dan mengaktifkan fitur *hot reloading*. Setiap kali Anda melakukan perubahan kode dan menyimpannya di IDE, game akan memuat ulang perubahan tersebut secara instan tanpa perlu memulai ulang:

```bash
./run-hot.sh
```

#### Menjalankan Target Spesifik secara Manual
Gunakan perintah Gradle berikut untuk menjalankan game pada target spesifik:

```bash
# Menjalankan target JVM (Desktop Launcher)
./gradlew runJvm

# Menjalankan target Kotlin/JS di Browser
./gradlew runJs

# Menjalankan target WebAssembly (Wasm) di Browser
./gradlew runWasm

# Menjalankan target Desktop Native
./gradlew runDesktop

# Menjalankan target iOS Simulator (Memerlukan macOS & Xcode)
./gradlew runIosSimulator

# Memasang dan menjalankan pada perangkat Android terhubung
./gradlew installAndroidDebug
```

#### 🤖 Android Emulator (Rekomendasi saat Uji Coba Android)
Kami menyediakan runner khusus yang mendeteksi AVD (Android Virtual Device) yang dikonfigurasi, menyalakan emulator, lalu memasang dan menjalankan game secara otomatis:

```bash
./run-android.sh
```

---

### 📦 Membangun Proyek / Build Project

Gunakan script `build.sh` untuk melakukan kompilasi dan pembuatan paket rilis/distribusi proyek:

```bash
./build.sh
```

Script ini mendukung menu interaktif untuk memilih target, atau argumen command-line untuk build otomatis:
* **JVM executable Fat JAR**: `./build.sh jvm`
* **Native macOS App Bundle**: `./build.sh macos`
* **Android Release AAB**: `./build.sh android-release`
* **Android Debug AAB**: `./build.sh android-debug`
* **Web (JS Bundle)**: `./build.sh web-js`
* **Web (Wasm Bundle)**: `./build.sh web-wasm`
* **Build All Targets**: `./build.sh all`

---

## 📜 Panduan Gaya Kode & Arsitektur / Code Style & Engine Patterns

Semua kontributor harus mengikuti aturan berikut demi menjaga kualitas dan konsistensi kode:

### 1. Gaya Penulisan Kode (Kotlin Code Style)
* **Penamaan**: Gunakan `PascalCase` untuk nama class, `camelCase` untuk fungsi/variabel/file class tunggal, dan `SCREAMING_SNAKE_CASE` untuk konstanta.
* **Bahasa Komentar**: Tulis komentar (`//` dan KDoc `/** */`) dalam **Bahasa Indonesia** untuk menyesuaikan konteks game. Logika kode tetap ditulis menggunakan bahasa Inggris.
* **Null Safety**: Hindari penggunaan non-null assertion `!!`. Selalu gunakan `?.let {}`, `?: return`, atau penanganan eksplisit.
* **Coroutines**: Gunakan `launch {}` di dalam `SceneCoroutineScope` atau `launchImmediately {}` pada `View` untuk coroutine yang terikat siklus hidup scene/view.

### 2. Pola Engine KorGE (KorGE Engine Patterns)
* **Pemisahan Logika & Visual**: State entitas hidup di kelas Kotlin murni, sedangkan KorGE `View` hanya menangani rendering.
* **Pengelolaan Input**: Deteksi input keyboard di dalam `addUpdater {}` menggunakan `views.input.keys.pressing(Key)` atau `justPressed(Key)`.
* **Deteksi Tabrakan (Collision)**: Bandingkan bounding rectangle (`entity1.globalBounds.intersects(entity2.globalBounds)`) di dalam update loop `addUpdater {}`.
* **Optimasi Memori**: Gunakan object pooling (`ObjectPool<T>`) untuk entitas yang sering dibuat dan dihancurkan seperti `Projectile` dan `FloatingDamageText`.

---

## 🎨 Spesifikasi Aset & Audio / Asset & Audio Rules

* **Pixel Art**: Semua sprite menggunakan grid 16×16. Wajib mematikan *smoothing* (`smoothing = false`) saat merender bitmap pixel art dan gunakan `MipMaps.NONE`.
* **Format Audio**: Gunakan format `.ogg` untuk seluruh musik latar (BGM) dan efek suara (SFX) guna kompresi dan kompatibilitas maksimal.
* **Definisi Jalur Aset**: Seluruh path file aset wajib didefinisikan sebagai konstanta di dalam `AssetPaths.kt`. Hindari penggunaan string path mentah (hardcoded) secara langsung di kode.

---

## 📖 Dokumentasi Pendukung / Further Documentation

* [KorGE Game Engine Documentation](https://docs.korge.org/korge/)
* [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
