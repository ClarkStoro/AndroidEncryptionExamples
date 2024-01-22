package com.clarkstoro.encryptionexample.di

import com.clarkstoro.data.repositories.ExampleRepositoryImpl
import com.clarkstoro.domain.repositories.ExampleRepository
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
    abstract fun provideExampleRepository(repo: ExampleRepositoryImpl): ExampleRepository
}