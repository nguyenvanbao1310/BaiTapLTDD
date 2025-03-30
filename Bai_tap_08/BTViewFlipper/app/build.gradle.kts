plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.iotstar.btviewflipper"
    compileSdk = 35

    defaultConfig {
        applicationId = "vn.iotstar.btviewflipper"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.viewpager2)
    implementation(libs.circleIndicator)
    implementation(libs.retrofit)
    implementation(libs.retrofitConverter)
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

}