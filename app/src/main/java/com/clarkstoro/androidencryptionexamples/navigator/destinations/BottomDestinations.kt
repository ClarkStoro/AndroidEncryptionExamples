package com.clarkstoro.androidencryptionexamples.navigator.destinations

import com.clarkstoro.androidencryptionexamples.R

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

data object AsymmetricEncryptDecryptScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.ASYMMETRIC_ENCRYPT_DECRYPT.name,
    arguments = emptyList()
), BottomNavItem {
    // Bottom navigation
    override val titleResId: Int
        get() = R.string.bottom_nav_page4
    override val selectedIconResId: Int
        get() = R.drawable.ic_key_chain
    override val deselectedIconResId: Int
        get() = R.drawable.ic_key_chain
}