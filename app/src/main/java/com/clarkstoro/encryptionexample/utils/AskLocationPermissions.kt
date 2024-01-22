package com.clarkstoro.encryptionexample.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import timber.log.Timber

@ExperimentalPermissionsApi
object AskLocationPermissions {
    private lateinit var locationPermissionsState: MultiplePermissionsState
    private lateinit var enableLocationRequest: ActivityResultLauncher<IntentSenderRequest>

    /**
     * Setup the system requests for enabling location and ask location permissions.
     * This method should be called within the setContent() method of the MainActivity.
     * Only after that can the request be launched.
     *
     * Once the request is launched there could be the following scenarios:
     * 1) device location is not enabled -> ask to the user to enable it, then will ask for permissions
     * 2) user grant for COARSE or FINE location, the corresponding callback is triggered
     * 3) user doesn't grant the permissions, the corresponding callback is triggered
     * 4) user have already granted the permission in the past:
     *      4.1) user granted FINE location -> onFineLocationGranted() is triggered without showing the system request
     *      4.2) user granted COARSE location -> on Android 13 and above the system ask for fine location,
     *              the callback triggered will be based on user choice.
     *       4.3) user already declined the permissions for 2 times (only for Android 12 and above)
     *
     *  Please note that only from Android 12 (API 31) users can request that your app retrieve only
     *  approximate location information, even when your app requests the ACCESS_FINE_LOCATION runtime permission.
     *
     * @param shouldAskAgainForFineLocation is a flag to check whether or not, once the user picked
     * COARSE location, the system should ask again for FINE location
     * @param onLocationEnableDenied is executed when the user decline to enable the location
     * @param onFineLocationGranted is executed when the user choose to grant FINE location
     * @param onCoarseLocationGranted is executed when the user choose to grant only COARSE location
     * @param onLocationPermissionNotGranted is executed when the permissions are not granted
     */

    @ExperimentalPermissionsApi
    @Composable
    fun SetupLocationPermissions(
        shouldAskAgainForFineLocation: Boolean = true,
        onLocationEnableDenied: (() -> Unit)? = null,
        onFineLocationGranted: (() -> Unit)? = null,
        onCoarseLocationGranted: (() -> Unit)? = null,
        onLocationPermissionNotGranted: (() -> Unit)? = null
    ) {

        enableLocationRequest = rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) launchLocationPermissions()
        }

        locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        val lifecycleOwner = LocalLifecycleOwner.current

        if (locationPermissionsState.allPermissionsGranted) {
            onFineLocationGranted?.invoke()
        } else {
            val allPermissionsRevoked = with(locationPermissionsState){
                permissions.size == revokedPermissions.size
            }

            if (!allPermissionsRevoked) {
                onCoarseLocationGranted?.invoke()
                DisposableEffect(key1 = lifecycleOwner) {
                    if (shouldAskAgainForFineLocation) launchLocationPermissions()
                    onDispose {  }
                }
            } else if (locationPermissionsState.shouldShowRationale) {
                onLocationEnableDenied?.invoke()
            } else { onLocationPermissionNotGranted?.invoke() }
        }
    }

    /**
     * Check if the user location is enabled, if not launch the system dialog to enabled it
     * When location is enabled check asks for location permissions
     */
    fun launchLocationEnableFlow(context: Context, onLocationEnableDenied: (() -> Unit)? = null) {
        val locationRequest = LocationRequest.create()
        val client = LocationServices.getSettingsClient(context)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val gpsSettingTask = client.checkLocationSettings(builder.build())
        gpsSettingTask.addOnSuccessListener { launchLocationPermissions() }
        gpsSettingTask.addOnFailureListener { e ->
            if (e is ApiException) {
                Timber.d(e.status.statusMessage)
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        /*
                            Location settings are not satisfied.
                            But could be fixed by showing the user a dialog.
                        */
                        try {
                            val resolvable = e as ResolvableApiException
                            val intentSenderRequest = IntentSenderRequest
                                .Builder(resolvable.resolution)
                                .build()
                            enableLocationRequest.launch(intentSenderRequest)
                        } catch (e: Exception) {
                            // TODO: Do something before calling onLocationEnableDenied().
                            onLocationEnableDenied?.invoke()
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        /*
                            Location settings are not satisfied.
                            However, we have no way to fix the settings so we won't show the dialog.
                        */
                        onLocationEnableDenied?.invoke()
                    }
                }
            }
        }
    }

    /**
     * Launch the system requests for enabling location and ask location permissions.
     *
     * WARNING: this method should be called only after SetupLocationPermissions() is called.
     * Furthermore, this method should be called within a side-effect or a non-composable callback.
     * Otherwise, it will throw an IllegalStateException.
     */
    private fun launchLocationPermissions() {
        if(::locationPermissionsState.isInitialized) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
}

