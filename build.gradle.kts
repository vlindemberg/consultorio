buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.navigation) apply false
    alias(libs.plugins.google.services) apply false
    kotlin("kapt") version "1.8.22" apply false
}
true // Needed to make the Suppress annotation work for the plugins block