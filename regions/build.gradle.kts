import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("maven-publish")
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/pia-foss/mobile-shared-regions")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

android {
    namespace = "com.kape.regions"

    compileSdk = 34
    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    group = "com.kape.android"
    version = "1.6.2"

    // Enable the default target hierarchy.
    // It's a template for all possible targets and their shared source sets hardcoded in the
    // Kotlin Gradle plugin.
    targetHierarchy.default()

    // Android
    android {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    // iOS
    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        tvosX64(),
        tvosSimulatorArm64(),
        tvosArm64()
    ).forEach {
        it.binaries.framework {
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.madgag.spongycastle:core:1.58.0.0")
                implementation("io.ktor:ktor-client-okhttp:2.3.3")
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.3")
            }
        }
        val iosTest by getting {
            dependencies {
            }
        }
        val tvosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.3")
            }
        }
    }
}
