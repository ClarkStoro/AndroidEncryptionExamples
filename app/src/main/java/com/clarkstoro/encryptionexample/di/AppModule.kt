package com.clarkstoro.encryptionexample.di

import android.app.Application
import android.content.Context
import com.clarkstoro.encryptionexample.navigator.Navigator
import com.clarkstoro.encryptionexample.providers.resource_provider.ResourceProvider
import com.clarkstoro.encryptionexample.providers.resource_provider.ResourceProviderImpl
import com.clarkstoro.encryptionexample.utils.BiometricCryptoManager
import com.clarkstoro.encryptionexample.utils.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Singleton
    @Provides
    fun provideComposeNavigator(): Navigator {
        return Navigator()
    }

    @Singleton
    @Provides
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Singleton
    @Provides
    fun provideBiometricCryptoManager(): BiometricCryptoManager = BiometricCryptoManager()
}