package com.clarkstoro.encryptionexample.navigator

import com.clarkstoro.encryptionexample.navigator.destinations.NavigationAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {

    companion object {
        const val NAVIGATOR_KEY = "navigation"
    }

    private val _navigateTo =
        MutableSharedFlow<NavigationAction>(extraBufferCapacity = 1)
    val navigateTo = _navigateTo.asSharedFlow()

    private val _navigateUp = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateUp = _navigateUp.asSharedFlow()

    fun navigateTo(navTarget: NavigationAction) {
        _navigateTo.tryEmit(navTarget)
    }

    fun navigateUp() {
        _navigateUp.tryEmit(Unit)
    }
}
