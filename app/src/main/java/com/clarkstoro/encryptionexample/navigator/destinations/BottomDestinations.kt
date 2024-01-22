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
        get() = R.drawable.settings
    override val deselectedIconResId: Int
        get() = R.drawable.settings
}

data object SaveRetrieveScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.SAVE_RETRIEVE.name,
    arguments = emptyList()
), BottomNavItem {

    // Bottom navigation
    override val titleResId: Int
        get() = R.string.bottom_nav_page2
    override val selectedIconResId: Int
        get() = R.drawable.settings
    override val deselectedIconResId: Int
        get() = R.drawable.settings
}

data object FingerprintScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.FINGERPRINT.name,
    arguments = emptyList()
), BottomNavItem {
    // Bottom navigation
    override val titleResId: Int
        get() = R.string.bottom_nav_page3
    override val selectedIconResId: Int
        get() = R.drawable.settings
    override val deselectedIconResId: Int
        get() = R.drawable.settings
}
