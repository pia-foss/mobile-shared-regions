pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        ivy {
            url = uri("https://kapps-releases.piaservers.net/releases")
            patternLayout {
                artifact("/[organisation]/[module]/[revision]/[module]-[revision].[ext]")
                setM2compatible(true)
            }

            // This is required in Gradle 6.0+ as metadata file (ivy.xml) is mandatory.
            metadataSources { artifact() }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Regions"
include(":regions")