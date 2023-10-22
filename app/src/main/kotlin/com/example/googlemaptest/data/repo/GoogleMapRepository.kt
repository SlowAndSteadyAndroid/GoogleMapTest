package com.example.googlemaptest.data.repo

import com.example.googlemaptest.data.model.NearbySearchResponse
import com.example.googlemaptest.data.model.place.PlaceInfoResponse
import retrofit2.Response

interface GoogleMapRepository {
    suspend fun nearbySearch(
        keyword: String,
        location: String,
    ): Response<NearbySearchResponse>

    suspend fun getPlaceDetail(
        placeId: String,
    ) : Response<PlaceInfoResponse>
}