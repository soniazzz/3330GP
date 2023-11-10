plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "hku.hk.cs.a3330gp"
    compileSdk = 34

    defaultConfig {
        applicationId = "hku.hk.cs.a3330gp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val googleApiKey = project.findProperty("GOOGLE_MAPS_API_KEY") ?: ""
        val mapboxPublicKey = project.findProperty("MAPBOX_ACCESS_TOKEN") ?: ""
        val mapboxPrivateKey = project.findProperty("MAPBOX_DOWNLOADS_TOKEN") ?: ""
        if (googleApiKey == "" || mapboxPublicKey == "" || mapboxPrivateKey == "") {
            throw GradleException("GOOGLE_MAPS_API_KEY or MAPBOX_DOWNLOADS_TOKEN or MAPBOX_DOWNLOADS_TOKEN isn't set. Set it in gradle.properties.")
        }
        resValue( "string", "google_maps_key", googleApiKey as String)
        resValue( "string", "mapbox_access_token", mapboxPublicKey as String)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.google.ar:core:1.40.0")

    implementation ("io.github.sceneview:arsceneview:1.2.2")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    // Mapbox Navigation SDK
    implementation ("com.mapbox.navigation:android:2.17.3")
    implementation ("com.mapbox.navigation:ui-dropin:2.17.3")
}