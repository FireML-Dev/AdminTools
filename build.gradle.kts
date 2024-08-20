import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.shadow)
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.william278.net/releases/")
    maven("https://repo.firedev.uk/repository/maven-public/")
    maven("https://maven.citizensnpcs.co/repo/")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.cmi.api)
    compileOnly(files("$projectDir/libs/even-more-fish-1.7.jar"))
    compileOnly(libs.denizen.api)
    compileOnly(libs.multiverse)
    compileOnly(files("$projectDir/libs/Jobs5.2.3.1.jar"))
    compileOnly(files("$projectDir/libs/CMILib1.4.7.16.jar"))
    compileOnly(libs.daisylib)
}

group = "uk.firedev"
version = "1.0.2-SNAPSHOT"
description = "Tools to help out Admins and Devs"
java.sourceCompatibility = JavaVersion.VERSION_21

paper {
    name = project.name
    version = project.version.toString()
    main = "uk.firedev.admintools.AdminTools"
    apiVersion = "1.21"
    author = "FireML"
    description = project.description.toString()
    foliaSupported = true

    serverDependencies {
        register("DaisyLib") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("EvenMoreFish") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("Multiverse-Core") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("Denizen") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("Jobs") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}

publishing {
    repositories {
        maven {
            name = "firedevRepo"

            // Repository settings
            var repoUrlString = "https://repo.firedev.uk/repository/maven-"
            repoUrlString += if (project.version.toString().endsWith("-SNAPSHOT")) {
                "snapshots/"
            } else {
                "releases/"
            }
            url = uri(repoUrlString)

            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        // If we need to use Spigot Mappings
        //manifest {
        //    attributes["paperweight-mappings-namespace"] = "spigot"
        //}
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
