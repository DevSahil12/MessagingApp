plugins {
    id ("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.messagingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.messagingapp"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.material.v190)

    //fix sizes in every devices
    implementation (libs.sdp.android)


    //Firebase dependencies
    //implementation platform("com.google.firebase:firebase-bom:33.2.0")
    implementation (libs.firebase.analytics)
    implementation(libs.play.services.auth)
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)
    implementation(libs.firebase.ui.auth)
    implementation(libs.google.firebase.auth) // Your Firebase services
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.storage)
    implementation(libs.google.firebase.storage)
    implementation(libs.firebase.database)

    //For circular image
    implementation (libs.circleimageview)
    implementation (libs.picasso)

}