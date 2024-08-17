rootProject.name = "AdminTools"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("paper-api", "io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
            library("daisylib", "uk.firedev:DaisyLib:2.0.3-SNAPSHOT")
            library("cmi-api", "CMI-API:CMI-API:9.7.0.1")
            library("evenmorefish", "com.oheers:EvenMoreFish:1.7")
            library("denizen-api", "com.denizenscript:denizen:1.3.1-SNAPSHOT")
            library("multiverse", "com.onarandombox.multiversecore:multiverse-core:4.3.12")
            library("jobs", "Jobs:jobs:5.2.3.1")
            library("cmilib", "CMILib:CMILib:1.4.7.16")
            library("carbonchat", "de.hexaoxi:carbonchat-api:3.0.0-beta.27")

            plugin("plugin-yml", "net.minecrell.plugin-yml.paper").version("0.6.0")
            plugin("shadow", "com.gradleup.shadow").version("8.3.0")
        }
    }
}