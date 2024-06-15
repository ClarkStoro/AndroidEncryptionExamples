package com.clarkstoro.androidencryptionexamples.di

import android.app.Application
import android.content.Context
import com.clarkstoro.androidencryptionexamples.navigator.Navigator
import com.clarkstoro.androidencryptionexamples.providers.resource_provider.ResourceProvider
import com.clarkstoro.androidencryptionexamples.providers.resource_provider.ResourceProviderImpl
import com.clarkstoro.androidencryptionexamples.utils.AsymmetricCryptoManager
import com.clarkstoro.androidencryptionexamples.utils.SymmetricAuthCryptoManager
import com.clarkstoro.androidencryptionexamples.utils.SymmetricCryptoManager
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
    fun provideCryptoManager(): SymmetricCryptoManager = SymmetricCryptoManager()

    @Singleton
    @Provides
    fun provideBiometricCryptoManager(): SymmetricAuthCryptoManager = SymmetricAuthCryptoManager()

    @Singleton
    @Provides
    fun provideAsymmetricCryptoManager(): AsymmetricCryptoManager = AsymmetricCryptoManager()
}