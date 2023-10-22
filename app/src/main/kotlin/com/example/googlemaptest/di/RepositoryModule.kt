package com.example.googlemaptest.di

import com.example.googlemaptest.data.repo.GoogleMapRepository
import com.example.googlemaptest.data.repo.GoogleMapRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGoogleMapRepository(googleMapRepositoryImpl: GoogleMapRepositoryImpl): GoogleMapRepository
}