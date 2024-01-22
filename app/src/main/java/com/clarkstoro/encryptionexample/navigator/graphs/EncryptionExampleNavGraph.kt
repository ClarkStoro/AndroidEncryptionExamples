package com.clarkstoro.encryptionexample.navigator.graphs

import androidx.biometric.BiometricViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.clarkstoro.encryptionexample.navigator.Navigator
import com.clarkstoro.encryptionexample.navigator.Navigator.Companion.NAVIGATOR_KEY
import com.clarkstoro.encryptionexample.navigator.destinations.EncryptDecryptScreen
import com.clarkstoro.encryptionexample.navigator.destinations.FingerprintScreen
import com.clarkstoro.encryptionexample.navigator.destinations.NavDestinationArgs
import com.clarkstoro.encryptionexample.navigator.destinations.SaveRetrieveScreen
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.presentation.encrypt_decrypt.EncryptDecryptScreen
import com.clarkstoro.encryptionexample.presentation.fingerprint.BiometricScreenViewModel
import com.clarkstoro.encryptionexample.presentation.fingerprint.FingerprintScreen
import com.clarkstoro.encryptionexample.presentation.save_retrieve.SaveRetrieveScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun EncryptionExampleNavGraph(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier
) {

    LaunchedEffect(NAVIGATOR_KEY) {
        navigator.navigateTo.onEach { navAction ->
            navController.navigate(navAction.destination, navAction.navOptions)

            navAction.args?.let {
                navController.currentBackStackEntry?.arguments?.apply {
                    putSerializable(NavDestinationArgs.DEFAULT_ARGS_KEY, it)
                }
            }

        }.launchIn(this)

        navigator.navigateUp.onEach {
            navController.navigateUp()
        }.launchIn(this)
    }


    NavHost(
        navController,
        startDestination = "EVENT_NAVIGATION_ROUTE",
        modifier = modifier
    ) {

        navigation(
            startDestination = EncryptDecryptScreen.route,
            route = "EVENT_NAVIGATION_ROUTE"
        ) {
            composable(EncryptDecryptScreen.route) { navBackStackEntry ->
                // Creates a ViewModel from the current BackStackEntry
                val commonViewModel = navBackStackEntry.sharedViewModel<CommonViewModel>(navController = navController)
                EncryptDecryptScreen(commonViewModel)
            }

            composable(SaveRetrieveScreen.route) { navBackStackEntry ->
                // Creates a ViewModel from the current BackStackEntry
                val commonViewModel = navBackStackEntry.sharedViewModel<CommonViewModel>(navController = navController)
                SaveRetrieveScreen(commonViewModel)
            }

            composable(FingerprintScreen.route) { navBackStackEntry ->
                // Creates a ViewModel from the current BackStackEntry
                val biometricViewModel = hiltViewModel<BiometricScreenViewModel>()
                FingerprintScreen(biometricViewModel)
            }
        }
    }
}


@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    return destination.parent?.route?.let { navGraphRoute ->
        val parentEntry = remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
        hiltViewModel(parentEntry)
    } ?: hiltViewModel()
}