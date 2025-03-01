    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        id("kotlin-kapt")
        id("androidx.navigation.safeargs")
    }

    android {
        namespace = "com.example.lab1"
        compileSdk = 35

        buildFeatures {
            viewBinding = true
            dataBinding = true
            buildConfig = true
        }

        defaultConfig {
            applicationId = "com.example.lab1"
            minSdk = 33
            targetSdk = 35
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
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    dependencies {
        // 1) Navigation Component
        // Для фрагментов + навигационной логики
        implementation(libs.androidx.navigation.fragment.ktx)
        // Для связки навигации с ActionBar/Toolbar
        implementation(libs.androidx.navigation.ui.ktx)

        // 2) RecyclerView
        // Для отображения списка календарных событий
        implementation(libs.androidx.recyclerview)

        // 3) DocumentFile
        // Удобная работа с URI (если нужно копировать файлы, например, при шаринге картинки)
        implementation(libs.androidx.documentfile)

        // Facebook Android SDK (базовый + модуль share)
        implementation(libs.facebook.core)
        implementation(libs.facebook.share)
        implementation(libs.facebook.android.sdk)


        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }