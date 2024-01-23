package com.clarkstoro.encryptionexample.navigator.destinations

import com.clarkstoro.encryptionexample.R

data object EncryptDecryptScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.ENCRYPT_DECRYPT.name,
    arguments = emptyList()
), BottomNavItem {

    // Bottom navigation
    override val titleResId: Int
        get() = R.string.bottom_nav_page1
    override val selectedIconResId: Int
        get() = R.drawable.ic_lock
    override val deselectedIconResId: Int
        get() = R.drawable.ic_lock
}

data object SaveRetrieveScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.SAVE_RETRIEVE.name,
    arguments = emptyList()
), BottomNavItem {

    // Bottom navigation
    override val titleResId: Int
        get() = R.string.bottom_nav_page2
    override val selectedIconResId: Int
        get() = R.drawable.ic_save
    override val deselectedIconResId: Int
        get() = R.drawable.ic_save
}

data object BiometricScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.BIOMETRIC.name,
    arguments = emptyList()
), BottomNavItem {
    // Bottom navigation
    override val titleResId: Int
        get() = R.string.bottom_nav_page3
    override val selectedIconResId: Int
        get() = R.drawable.ic_biometric
    override val deselectedIconResId: Int
        get() = R.drawable.ic_biometric
}
