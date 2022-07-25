package com.sotatek.weather.di

import com.sotatek.weather.base.Storage
import com.sotatek.weather.data.local.AppPreManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindAppPreManager(storage: AppPreManager): Storage
}