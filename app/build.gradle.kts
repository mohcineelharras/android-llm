plugins {
        id("com.android.application")
        id("org.jetbrains.kotlin.android")
    }

    android {
        namespace = "com.example.llmapp"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.example.llmapp"
            minSdk = 24
            targetSdk = 34
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
            viewBinding = true
        }
    }

    dependencies {
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        // Material 3
        implementation("androidx.compose.material3:material3:1.2.0")

        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // OkHttp
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

        // Gson
        implementation("com.google.code.gson:gson:2.8.9")

        // Coil
        implementation("io.coil-kt:coil:1.4.0")
    }
