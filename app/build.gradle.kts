import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)

    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters"
    compileSdk = 35

    buildFeatures {
        buildConfig = true

        defaultConfig {
            applicationId = "com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters"
            minSdk = 24
            targetSdk = 35
            versionCode = 1
            versionName = "1.0"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11

            // Required for Supabase/kotlinx.datetime on Android API below 26
            isCoreLibraryDesugaringEnabled = true
        }

        buildFeatures {
            compose = true
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
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    dependencies {
        // Core Android
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)

        // Compose
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)
        implementation("androidx.compose.material:material-icons-extended")

        // Navigation + ViewModel
        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.lifecycle.viewmodel.compose)

        // RoomDB - keep for now because the rest of your app still uses Room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        ksp(libs.androidx.room.compiler)

        // Supabase
        implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
        implementation("io.github.jan-tennert.supabase:postgrest-kt")
        implementation("io.github.jan-tennert.supabase:auth-kt")
        implementation("io.ktor:ktor-client-android:3.0.3")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
        implementation("io.github.jan-tennert.supabase:storage-kt:3.1.4")

        // Required for java.time.Instant on Android API below 26
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

        // Images
        implementation("io.coil-kt:coil-compose:2.6.0")

        // Graphs
        implementation("com.patrykandpatryk.vico:compose-m3:2.2.0")

        // Unit tests
        testImplementation(libs.junit)


        implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
        implementation("io.github.jan-tennert.supabase:postgrest-kt")
        implementation("io.github.jan-tennert.supabase:auth-kt")
        implementation("io.ktor:ktor-client-android:3.0.3")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")


        // Android tests

        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)

        // Debug
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
    }
}