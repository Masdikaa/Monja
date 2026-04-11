import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties()
val localPropertiesFile: File = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { stream ->
        localProperties.load(stream)
    }
}

val mapboxPublicToken = localProperties.getProperty("MAPBOX_PUBLIC_TOKEN") ?: ""

android {
    namespace = "com.masdika.monja"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.masdika.monja"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        resValue("string", "mapbox_access_token", mapboxPublicToken)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // ===================== DATA LAYER ======================
    implementation(project(":data"))

    // ================== CORE & LIFECYCLE ===================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // =================== JETPACK COMPOSE ===================
    implementation(libs.androidx.activity.compose)
    // Compose BOM (Bill of Materials)
    implementation(platform(libs.androidx.compose.bom))
    // UI Graphics
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    // Preview
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Material Design 3
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.material3)

    // ======================= HILT DI =======================
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ===================== NAVIGATION ======================
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // ===================== BOTTOM BAR ======================
    implementation(libs.compose.animated.navigationbar)

    // ====================== MAP BOX ========================
    implementation(libs.mapbox.android)
    implementation(libs.maps.compose.ndk27)

    // ======================= TESTING =======================
    // Unit Test (Local)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.androidx.lifecycle.viewmodel.testing)
    testImplementation(libs.androidx.lifecycle.runtime.testing)
    testImplementation(libs.androidx.hilt.testing)
    // Instrumented Test (Run in Emulator/Device)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Testing Utilities
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.ktor.client.mock)

    // =================== DEBUGGING TOOLS ===================
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}