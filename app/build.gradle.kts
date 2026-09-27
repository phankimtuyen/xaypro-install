import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
}

kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "xaypro.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.AppImage)
            packageName = "XayPro"
            packageVersion = "1.2.0"
            description = "XâyPro — Ứng dụng quản lý xây dựng"
            vendor = "XâyPro"
        }
    }
}
