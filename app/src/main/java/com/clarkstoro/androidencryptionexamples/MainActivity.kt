package com.clarkstoro.androidencryptionexamples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.clarkstoro.androidencryptionexamples.navigator.Navigator
import com.clarkstoro.androidencryptionexamples.navigator.destinations.BottomNavItem
import com.clarkstoro.androidencryptionexamples.navigator.destinations.EncryptDecryptScreen
import com.clarkstoro.androidencryptionexamples.navigator.destinations.NavGraphDestinations
import com.clarkstoro.androidencryptionexamples.navigator.destinations.NavigationAction
import com.clarkstoro.androidencryptionexamples.navigator.graphs.EncryptionExampleNavGraph
import com.clarkstoro.androidencryptionexamples.ui.theme.AppM3Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppM3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    EncryptionExampleMain(navController, navigator)
                }
            }
        }
    }
}



@ExperimentalMaterial3Api
@Composable
fun EncryptionExampleMain(
    navController: NavHostController = rememberNavController(),
    navigator: Navigator
) {

    val bottomBarVisibilityState = rememberSaveable { (mutableStateOf(false)) }
    val arrowBackVisibilityState = rememberSaveable { (mutableStateOf(false)) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    bottomBarVisibilityState.value = NavGraphDestinations.BOTTOM_NAV_DESTINATIONS.any {
        it.route == currentRoute
    }

    arrowBackVisibilityState.value =
        NavGraphDestinations.BOTTOM_NAV_DESTINATIONS.any { it.route == currentRoute } == false


    Scaffold(
        containerColor = if (isSystemInDarkTheme()) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.onPrimary
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name)
                    )
                }
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                navigator = navigator,
                visible = bottomBarVisibilityState.value
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            EncryptionExampleNavGraph(
                navController = navController,
                navigator = navigator,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, navigator: Navigator, visible: Boolean) {
    val items = NavGraphDestinations.BOTTOM_NAV_DESTINATIONS
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            items.forEach { item ->
                val isSelected = item.route == currentRoute
                if (item is BottomNavItem) {

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navigator.navigateTo(
                                object : NavigationAction {
                                    override val destination: String get() = item.route
                                    override val navOptions
                                        get() = NavOptions.Builder()
                                            .setPopUpTo(EncryptDecryptScreen.route, false)
                                            .setLaunchSingleTop(true)
                                            .build()

                                }
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.selectedIconResId ),
                                contentDescription = stringResource(id = item.titleResId),
                                modifier = Modifier,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }
        }
    }
}