package com.clarkstoro.encryptionexample.di

import com.clarkstoro.data.datasources.ExampleDatasource
import com.clarkstoro.data.network.ExampleDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @Binds
    @Singleton
    abstract fun provideExampleDatasource(ds: ExampleDatasourceImpl): ExampleDatasource
}