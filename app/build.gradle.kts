plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}



android {
    namespace = "com.example.mitraartapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mitraartapp"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        /*testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        addManifestPlaceholders(mapOf(
            "VKIDClientID" to "51794395", // ID вашего приложения (app_id).
            "VKIDClientSecret" to "dJF1b8VKlFN1xmCJKx1m", // Ваш защищенный ключ (client_secret).
            "VKIDRedirectHost" to "vk.com", // Обычно используется vk.com.
            "VKIDRedirectScheme" to "vk51794395", // Обычно используется vk{ID приложения}.
        ))*/

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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs\\jtds-1.2.5.jar\\jtds-1.2.5.jar"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Optional: Circle Indicator (To fix the xml preview "Missing classes" error)
    implementation ("me.relex:circleindicator:2.1.6")
    implementation("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("io.ktor:ktor-client-android:1.4.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.credentials:credentials:1.2.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0")
    implementation ("com.google.android.gms:play-services-location:17.0.0")
    implementation ("com.github.therealbush:translator:1.0.2")
 }



