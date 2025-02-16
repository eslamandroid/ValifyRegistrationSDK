plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp.kotlin.symbol)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.navigation.args)
    id("kotlin-parcelize")
    id("maven-publish")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.valify.registration"
            artifactId = "RegistrationSDK"
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
        }
        repositories {
            maven {
                url = uri("${rootProject.layout.buildDirectory}/repo")
            }
            mavenLocal()
        }
    }
}

android {
    namespace = "com.valify.registration"
    compileSdk = 35

    defaultConfig {
        minSdk = 22
        lint.targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.security.crypto)

    // Room
    implementation(libs.androidx.jetpack.room.runtime)
    implementation(libs.androidx.jetpack.room.coroutine)
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite.ktx)
    ksp(libs.androidx.jetpack.room.compiler)

    //CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.face.detection)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel)


    // For responsive layouts
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}