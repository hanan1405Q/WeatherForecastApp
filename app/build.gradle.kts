plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherapp"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    //hilt
//    implementation ("com.google.dagger:hilt-android:2.44")
//    kapt ("com.google.dagger:hilt-android-compiler:2.44")
//    kapt ("androidx.hilt:hilt-compiler:1.0.0")
//    implementation ("androidx.hilt:hilt-work:1.0.0")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // Architectural Components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    //view-model
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")

    //Room
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Activity KTX for viewModels()
    implementation ("androidx.activity:activity-ktx:1.7.2")

    //lottie
    implementation ("com.airbnb.android:lottie:6.0.0")

    //workManager
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    //google services
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.libraries.places:places:3.1.0")

    //work manager
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    // Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

//    // Local Unit Tests
//    implementation "androidx.test:core:1.5.0"
//    testImplementation "junit:junit:4.13.2"
//    testImplementation "org.hamcrest:hamcrest-all:1.3"
//    testImplementation "androidx.arch.core:core-testing:2.2.0"
//    testImplementation "org.robolectric:robolectric:4.3.1"
//    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"
//    testImplementation "com.google.truth:truth:1.0.1"
//    testImplementation "org.mockito:mockito-core:3.12.4"
//    testImplementation "io.mockk:mockk:1.13.5"
//    testImplementation "app.cash.turbine:turbine:0.12.1"
//
//    // Instrumented Unit Tests
//    androidTestImplementation "junit:junit:4.13.2"
//    androidTestImplementation "com.linkedin.dexmaker:dexmaker-mockito:2.12.1"
//    androidTestImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"
//    androidTestImplementation "androidx.arch.core:core-testing:2.2.0"
//    androidTestImplementation "com.google.truth:truth:1.0.1"
//    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
//    androidTestImplementation "org.mockito:mockito-core:3.12.4"
//    androidTestImplementation 'com.linkedin.dexmaker:dexmaker-mockito:2.28.3'
//    androidTestImplementation 'app.cash.turbine:turbine:1.0.0'
//    androidTestImplementation "io.mockk:mockk-android:1.13.5"
}