package com.example.googlemaptest.data.repo

import com.example.googlemaptest.api.GooglePlaceService
import com.example.googlemaptest.data.model.NearbySearchResponse
import com.example.googlemaptest.data.model.place.PlaceInfoResponse
import retrofit2.Response
import javax.inject.Inject

class GoogleMapRepositoryImpl @Inject constructor(private val googleMapService: GooglePlaceService) :
    GoogleMapRepository {
    override suspend fun nearbySearch(
        keyword: String,
        location: String
    ): Response<NearbySearchResponse> =
        googleMapService.getNearbySearch(keyword, location)

    override suspend fun getPlaceDetail(placeId: String): Response<PlaceInfoResponse> =
        googleMapService.getPlaceDetail(placeId)
}