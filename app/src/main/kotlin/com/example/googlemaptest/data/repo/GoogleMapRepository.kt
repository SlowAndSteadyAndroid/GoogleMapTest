package com.example.googlemaptest.data.repo

import com.example.googlemaptest.data.model.NearbySearchResponse
import com.example.googlemaptest.data.model.place.PlaceInfoResponse
import com.example.googlemaptest.room.allergie.AllergieEntity
import com.example.googlemaptest.room.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GoogleMapRepository {

    val allergyList : Flow<List<AllergieEntity>>

    val bookmarkList : Flow<List<BookmarkEntity>>

    suspend fun nearbySearch(
        keyword: String,
        location: String,
    ): Response<NearbySearchResponse>

    suspend fun getPlaceDetail(
        placeId: String,
    ) : Response<PlaceInfoResponse>

    suspend fun addBookmark(
        entity: BookmarkEntity
    )

    suspend fun deleteBookmark(
        entity: BookmarkEntity
    )

    suspend fun addAllergie(
        name: String
    )

    suspend fun deleteAllergie(
        allergieEntity: AllergieEntity
    )
}