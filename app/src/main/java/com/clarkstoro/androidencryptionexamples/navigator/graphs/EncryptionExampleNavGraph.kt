package com.clarkstoro.androidencryptionexamples.navigator.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.clarkstoro.androidencryptionexamples.navigator.Navigator
import com.clarkstoro.androidencryptionexamples.navigator.Navigator.Companion.NAVIGATOR_KEY
import com.clarkstoro.androidencryptionexamples.navigator.destinations.AsymmetricEncryptDecryptScreen
import com.clarkstoro.androidencryptionexamples.navigator.destinations.BiometricScreen
import com.clarkstoro.androidencryptionexamples.navigator.destinations.EncryptDecryptScreen
import com.clarkstoro.androidencryptionexamples.navigator.destinations.MAIN_NAVIGATION_ROUTE_KEY
import com.clarkstoro.androidencryptionexamples.navigator.destinations.NavDestinationArgs
import com.clarkstoro.androidencryptionexamples.navigator.destinations.SaveRetrieveScreen
import com.clarkstoro.androidencryptionexamples.presentation.asymmetric_encrypt_decrypt.AsymmetricEncryptDecryptScreen
import com.clarkstoro.androidencryptionexamples.presentation.encrypt_decrypt.EncryptDecryptScreen
import com.clarkstoro.androidencryptionexamples.presentation.fingerprint.BiometricScreen
import com.clarkstoro.androidencryptionexamples.presentation.save_retrieve.SaveRetrieveScreen
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
        startDestination = MAIN_NAVIGATION_ROUTE_KEY,
        modifier = modifier
    ) {

        navigation(
            startDestination = EncryptDecryptScreen.route,
            route = MAIN_NAVIGATION_ROUTE_KEY
        ) {
            composable(EncryptDecryptScreen.route) {
                EncryptDecryptScreen(hiltViewModel())
            }

            composable(SaveRetrieveScreen.route) {
                SaveRetrieveScreen(hiltViewModel())
            }

            composable(BiometricScreen.route) {
                BiometricScreen(hiltViewModel())
            }

            composable(AsymmetricEncryptDecryptScreen.route) {
                AsymmetricEncryptDecryptScreen(hiltViewModel())
            }
        }
    }
}