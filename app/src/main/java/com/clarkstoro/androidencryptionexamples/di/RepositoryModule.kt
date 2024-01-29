package com.clarkstoro.androidencryptionexamples.di

import com.clarkstoro.data.repositories.DataStoreManagerRepositoryImpl
import com.clarkstoro.domain.repositories.DataStoreManagerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideDataStoreManagerRepository(
        repo: DataStoreManagerRepositoryImpl
    ): DataStoreManagerRepository
}