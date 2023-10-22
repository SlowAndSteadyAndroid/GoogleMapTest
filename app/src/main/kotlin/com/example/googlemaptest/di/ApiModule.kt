package com.example.googlemaptest.di

import com.example.googlemaptest.api.GooglePlaceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideGoogleMapService(): GooglePlaceService = GooglePlaceService.create()

}