package com.example.googlemaptest.api

import com.example.googlemaptest.data.model.NearbySearchResponse
import com.example.googlemaptest.data.model.place.PlaceInfoResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlaceService {

    @GET(NEARBY_SEARCH)
    suspend fun getNearbySearch(
        @Query("keyword") keyword: String,
        @Query("location") location: String,
        @Query("radius") radius: Int = DEFAULT_RADIUS,
        @Query("type") type: String = DEFAULT_TYPE,
        @Query("key") key: String = API_KEY
    ): Response<NearbySearchResponse>

    @GET(DETAIL_SEARCH)
    suspend fun getPlaceDetail(
        @Query("placeid") placeId: String,
        @Query("key") key: String = API_KEY
    ) : Response<PlaceInfoResponse>


    companion object {

        private const val BASE_URL = "https://maps.googleapis.com/"

        private const val NEARBY_SEARCH = "maps/api/place/nearbysearch/json"
        private const val DETAIL_SEARCH = "maps/api/place/details/json"

        private const val API_KEY = "AIzaSyAT8cvuip86EdVndZM4VcNmvt_RXpU-Jbg"

        private const val DEFAULT_RADIUS = 5000

        private const val DEFAULT_TYPE = "restaurant|bakery|cafe|meal_delivery|meal_takeaway"
        fun create(): GooglePlaceService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GooglePlaceService::class.java)
    }
}