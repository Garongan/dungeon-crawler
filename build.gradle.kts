import korlibs.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}

korge {
	id = "com.sample.demo"

// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets
	
	targetJvm()
	// Target lainnya dinonaktifkan (di-comment out) untuk fokus ke JVM (desktop)
	// targetJs()
	// targetWasmJs() // targetWasm() didepresiasi, gunakan targetWasmJs() jika ingin mengaktifkan
	// targetIos()
	// targetAndroid()

	// Konfigurasi default background color pada build-level (misal untuk splash screen/canvas default)
	// Catatan: Menggunakan format ARGB Int (0xff1a1a2e adalah warna gelap Katakombe / Zone 2)
	backgroundColor = 0xff1a1a2e.toInt()

	// Mengaktifkan kotlinx-serialization secara otomatis untuk seluruh project multiplatform
	serializationJson()

	// Mengatur orientasi game ke Portrait untuk target mobile/device
	orientation = Orientation.PORTRAIT
}

dependencies {
	// Korge-core otomatis dimasukkan secara transitif oleh Korge Gradle Plugin.
	// Korge-box2d diaktifkan dengan meng-uncomment di deps.kproject.yml
	// Kotlinx-serialization diaktifkan secara otomatis melalui korge.serializationJson() di atas.
	add("commonMainApi", project(":deps"))
    //add("commonMainApi", project(":korge-dragonbones"))
}
