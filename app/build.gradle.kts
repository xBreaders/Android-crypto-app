plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.example.cryptoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cryptoapp"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    subprojects {
        apply(plugin = "org.jetbrains.dokka")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-android:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.test:runner:1.5.2")
    implementation("androidx.navigation:navigation-testing:2.7.6")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation("androidx.work:work-testing:2.9.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.5.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-common:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    val pagingVersion = "3.2.1"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:3.3.0-alpha02")
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.10")
    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.5")
    implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.5")
    implementation("com.patrykandpatrick.vico:views:2.0.0-alpha.5")
    //testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.robolectric:robolectric:4.11.1")
}