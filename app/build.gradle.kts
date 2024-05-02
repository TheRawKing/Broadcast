plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.therawking.broadcast"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.therawking.broadcast"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-auth:22.3.1")
    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.karumi:dexter:6.2.3")
    api ("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}