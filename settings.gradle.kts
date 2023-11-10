pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")

            authentication {
                create<BasicAuthentication>("basic")
            }

            credentials {
                username = "mapbox"
                password = extra["MAPBOX_DOWNLOADS_TOKEN"] as String? ?: System.getenv("MAPBOX_DOWNLOADS_TOKEN")
                if (password == null || password == "") {
                    throw GradleException("MAPBOX_DOWNLOADS_TOKEN isn't set. Set it in gradle.properties.")
                }
            }
        }
    }
}

rootProject.name = "3330GP"
include(":app")
