@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.example.verboversa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.verboversa"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            dataBinding = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
    buildFeatures {
        viewBinding = true
    }


}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

dependencies {

    // https://mvnrepository.com/artifact/com.google.android.gms/play-services-auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Facebook Android SDK (everything)
    implementation ("com.facebook.android:facebook-android-sdk:[8,9)")
    //implementation ("com.facebook.android:facebook-android-sdk:16.3.0") // Use the latest version available
    implementation ("com.facebook.android:facebook-login:latest.release")


    implementation ("androidx.appcompat:appcompat:1.2.0")

    implementation ("com.google.android.material:material:1.4.0")

    implementation ("com.google.cloud:google-cloud-translate:2.0.1") // Use the latest version

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation ("androidx.camera:camera-camera2:1.0.0-beta07")
    implementation ("androidx.camera:camera-lifecycle:1.0.0-beta07")
    implementation ("androidx.camera:camera-view:1.0.0-alpha20")


    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    implementation ("androidx.core:core-ktx:1.6.0")

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.mlkit.text.recognition)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}