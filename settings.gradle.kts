// By default, this is how your built jars are called
rootProject.name = "CobblemonWildLoot"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        gradlePluginPortal()
    }
}

listOf(
    "common",
    "neoforge",
    "fabric"
).forEach { include(it)}
