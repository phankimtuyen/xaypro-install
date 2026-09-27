import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeDisambiguationRule
import org.gradle.api.attributes.MultipleCandidatesDetails

// Biến thể "awt" của skiko không khai báo attribute "ui" nên khi resolve không có
// attribute yêu cầu, nó nhập nhằng với biến thể "android" (ui=android). Gắn ui=awt
// cho biến thể awt để luật ưu tiên bên dưới chọn được nó.
abstract class SkikoAwtMetadataRule : ComponentMetadataRule {
    override fun execute(context: ComponentMetadataContext) {
        val ui = Attribute.of("ui", String::class.java)
        listOf("awtRuntimeElements-published", "awtApiElements-published").forEach { v ->
            runCatching { context.details.withVariant(v) { attributes { attribute(ui, "awt") } } }
        }
    }
}

abstract class SkikoUiAwtRule : AttributeDisambiguationRule<String> {
    override fun execute(details: MultipleCandidatesDetails<String>) {
        if ("awt" in details.candidateValues) details.closestMatch("awt")
    }
}

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
    id("dev.hydraulic.conveyor") version "2.0"
}

group = "com.xaypro.app"
version = "1.2.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

    // Native Skiko cho Windows để Conveyor đóng gói bản Windows từ Linux.
    // Dùng artifact runtime cụ thể (không nhập nhằng biến thể như module "skiko").
    windowsAmd64("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.7.97")
}

// Ưu tiên biến thể "awt" của Skiko cho mọi resolve (kể cả detached config Conveyor).
val uiAttr = Attribute.of("ui", String::class.java)
dependencies {
    components {
        withModule("org.jetbrains.skiko:skiko", SkikoAwtMetadataRule::class.java)
    }
    attributesSchema {
        attribute(uiAttr) { disambiguationRules.add(SkikoUiAwtRule::class.java) }
    }
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
