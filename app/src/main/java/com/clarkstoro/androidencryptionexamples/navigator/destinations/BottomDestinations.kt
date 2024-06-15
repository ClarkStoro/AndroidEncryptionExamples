package com.clarkstoro.androidencryptionexamples.navigator.destinations

import com.clarkstoro.androidencryptionexamples.R

data object SymmetricScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.SYMMETRIC_BASE.name,
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

data object SymmetricStorageScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.SYMMETRIC_STORAGE.name,
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

data object SymmetricAuthScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.SYMMETRIC_AUTH.name,
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

data object AsymmetricScreen: NavGraphDestinations(
    route = NavGraphDestinationKeys.ASYMMETRIC_BASE.name,
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