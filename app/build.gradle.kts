plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs")
}

android {
    namespace = "com.sabal.terramasterhub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sabal.terramasterhub"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Enable data binding
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    //RETROFIT AND GSON (NETWORKING LIBRARY FOR CONSUMING RESTFUL API)
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")

    //KOTLIN COROUTINES (LIBRARY FOR WRITING ASYNCH CODES EASILY)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")

    //VIEWMODEL AND LIVEDATA ()
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")

    //NEW MATERIAL DESIGN
    implementation("com.google.android.material:material:1.1.0-alpha07")

    //KodeIn DEPENDENCY INJECTION
    implementation("org.kodein.di:kodein-di-generic-jvm:6.2.1")
    implementation("org.kodein.di:kodein-di-framework-android-x:6.2.1")

    //ANDROID ROOM
    implementation("androidx.room:room-runtime:2.1.0-rc01")
    implementation("androidx.room:room-ktx:2.1.0-rc01")
    kapt("androidx.room:room-compiler:2.1.0-rc01")

    //ANDROID NAVIGATION ARCHITECTURE
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0-alpha05")
    implementation("androidx.navigation:navigation-ui-ktx:2.1.0-alpha05")


    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}