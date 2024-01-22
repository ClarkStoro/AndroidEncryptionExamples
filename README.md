# mobile-boilerplate-android-compose

This project is a starting point for any native Android application by Bitrock using Jetpack Compose.

## Features

This project starts with a login screen containing:
- e-mail and password inputs
- password reset button (password reset yet to be implemented)
- login and signup buttons

From the signup button, the user can navigate to a signup screen containing:
- add/edit photo field [^note]
- e-mail and password inputs
- signup button (signup yet to be implemented)

Once logged in, the user lands to the main screen. It has a bottom navigation bar with three
options, representing three example screens:
- a search screen
- a home screen (which at the moment has a RecyclerView with example data)
- a settings screen (with a button to navigate to a further Detail screen)

## Libraries

For this project the following libraries are used:

- [Jetpack Navigation](https://developer.android.com/guide/navigation) to handle navigation between different Fragments
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
  to handle the Dependency Injection within the application
- [Timber](https://github.com/JakeWharton/timber) as a logger
- [Retrofit](https://square.github.io/retrofit/) as an abstraction of an HTTP client based on annotations [^note2]
- [OkHttp](https://square.github.io/okhttp/) as an HTTP client to perform HTTP requests and get responses
- [Room](https://developer.android.com/training/data-storage/room) for local database management
- [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
  to store simple data in key-value pairs (such as the user's e-mail and password in this example)
- [Landscapist-Glide](https://github.com/skydoves/landscapist) to allow network pictures loading
- [SplashScreen](https://developer.android.com/develop/ui/views/launch/splash-screen) to implement a SplashScreen
- [FusedLocationProvider](https://developers.google.com/android/guides/setup) to get and track the user's location (also on background)
- [Accompanist Permissions](https://google.github.io/accompanist/permissions/) to handle the request of location permissions
- [Compose Material 3](https://developer.android.com/jetpack/compose/designsystems/material3) for migration from Material Design 2 to Material Design 3
- [Compose Material](https://developer.android.com/jetpack/androidx/releases/compose-material) still being kept for the BottomSheetLayout

[^note] The photo picker implementation used in this project is [this one](https://developer.android.com/training/data-storage/shared/photopicker).
[^note2] The base URL required has to be inserted inside the build.gradle file of the data module.