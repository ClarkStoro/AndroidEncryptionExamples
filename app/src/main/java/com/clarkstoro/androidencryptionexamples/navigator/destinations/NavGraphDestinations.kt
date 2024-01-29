package com.clarkstoro.androidencryptionexamples.navigator.destinations

import android.os.Build
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions

enum class NavGraphDestinationKeys {
    ENCRYPT_DECRYPT,
    SAVE_RETRIEVE,
    BIOMETRIC,
    ASYMMETRIC_ENCRYPT_DECRYPT
}

const val MAIN_NAVIGATION_ROUTE_KEY = "main_nav_route_key"

sealed class NavGraphDestinations(open val route: String, open val arguments: List<NamedNavArgument>) {

    companion object {
        val BOTTOM_NAV_DESTINATIONS = listOf<NavGraphDestinations>(
            EncryptDecryptScreen,
            SaveRetrieveScreen,
            BiometricScreen,
            AsymmetricEncryptDecryptScreen
        )
    }

    inline fun <reified T: NavDestinationArgs> parseArguments(backStackEntry: NavBackStackEntry): T {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getSerializable(NavDestinationArgs.DEFAULT_ARGS_KEY, T::class.java)
            } else {
                backStackEntry.arguments?.getSerializable(NavDestinationArgs.DEFAULT_ARGS_KEY) as T
            }.let {
                it ?: throw Exception("Could not parse arguments")
            }
        } catch (e: Exception) {
            throw Exception("Could not parse arguments")
        }
    }
}

interface NavigationAction {
    val destination: String
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
    val args: NavDestinationArgs?
        get() = null
}

interface BottomNavItem {
    val titleResId: Int
    val selectedIconResId: Int
    val deselectedIconResId: Int
}

sealed class NavDestinationArgs: java.io.Serializable {

    companion object {
        const val DEFAULT_ARGS_KEY = "default_args_key"
    }
}