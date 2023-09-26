import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktor_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

group = "com.silvered"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation("com.mayakapps.compose:window-styler:0.3.2")
    implementation("org.jmdns:jmdns:3.5.8")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.auth0:java-jwt:4.4.0")

}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "BluetoothSwitcher"
            packageVersion = "1.0.0"
            description = "Cross Device Bluetooth Switcher"
            copyright = "© 2023 Simone Silvestri. All rights reserved."
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            windows {
                iconFile.set(project.file("BluetoothSwitcher.ico"))
            }
        }
    }
}
