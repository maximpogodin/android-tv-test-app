# Android TV Test App
## Contents
1. [Description](#description)
2. [Plugins, library and stuff](#plugins-library-and-stuff)
3. [SDK specification](#sdk-specification)
4. [Setting up the project](#setting-up-the-project)
## Description
The test Android TV project was developed in Android Studio using Kotlin. The build is based on the Gradle build system.
## Plugins, library and stuff:
- Kotlin - ver. 1.9.22
- GSON - ver. 2.9.0
- Retrofit - ver. 2.9.0
- The application was launched on the TV 1080p emulator API 34
## SDK specification:
- Target sdk version 34
- Min sdk version 26
## Setting up the project
1. Start Android Studio
2. Select "Get from Version Control"
3. Configure properties:
    * Version control: Git
    * URL:(https://github.com/maximpogodin/android-tv-test-app)
    * Directory: your_path</br>
4. Click on Clone button
5. Open project
6. Set external api options here: [app/src/main/java/com/example/androidtvtestapp/network/ExternalApiOptions.kt](https://github.com/maximpogodin/android-tv-test-app/blob/main/app/src/main/java/com/example/androidtvtestapp/network/ExternalApiOptions.kt)
