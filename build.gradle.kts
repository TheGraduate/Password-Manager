// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }

    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}