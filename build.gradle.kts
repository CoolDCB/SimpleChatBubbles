plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
    id("xyz.jpenilla.run-paper") version("2.3.1")
}

group = "org.lushplugins"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/") // PaperMC
    maven("https://repo.fancyplugins.de/snapshots") // FancyHolograms
    maven("https://repo.codemc.io/repository/maven-releases/") // PacketEvents
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("de.oliver:FancyHolograms:2.4.2.129")
    compileOnly("com.github.retrooper:packetevents-spigot:2.7.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        val folder = System.getenv("pluginFolder_1-20")
        if (folder != null) destinationDirectory.set(file(folder))
        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        dependsOn("copyFontWidths")

        exclude("**/font-widths.txt")
        expand(project.properties)

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }

    register<Copy>("copyFontWidths") {
        from("src/main/resources")
        include("font-widths.txt")
        into("$buildDir/resources/main")
    }

    runServer {
        minecraftVersion("1.21.1")

        downloadPlugins{
            modrinth("fancyholograms", "2.4.2.129")
            modrinth("packetevents", "2.7.0")
            modrinth("viaversion", "5.2.2-SNAPSHOT+662")
            modrinth("viabackwards", "5.2.2-SNAPSHOT+380")
        }
    }
}