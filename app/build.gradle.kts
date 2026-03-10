plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
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

    // ======= NETWORKING & DATABASE (SUPABASE + KTOR) =======
    // Serialization (Used by Supabase & Ktor for parsing JSON)
    implementation(libs.kotlinx.serialization.json)

    // ================== KTOR HTTP Client ===================
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)

    // ====================== SUPABASE =======================
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)

    // ======================= TESTING =======================
    // Unit Test (Local)
    testImplementation(libs.junit)
    // Instrumented Test (Run in Emulator/Device)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // =================== DEBUGGING TOOLS ===================
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}