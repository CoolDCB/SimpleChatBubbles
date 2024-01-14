plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") } // PaperMC
    maven { url = uri("https://repo.fancyplugins.de/snapshots") } // FancyHolograms
//    maven { url = uri("https://repo.fancyplugins.de/releases") } // FancyHolograms
    maven { url = uri("https://jitpack.io") } // ChatColorHandler
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
//    compileOnly("de.oliver:FancyHolograms:2.0.4.44")
    compileOnly(files("libs/FancyHolograms-2.0.4.44.jar"))
    implementation("com.github.CoolDCB:ChatColorHandler:v2.5.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("space.arim", "me.dave.activityrewarder.libraries.paperlib")
        relocate("org.enchantedskies", "me.dave.activityrewarder.libraries.enchantedskies")
        relocate("me.dave.chatcolorhandler", "me.dave.activityrewarder.libraries.chatcolor")

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
}