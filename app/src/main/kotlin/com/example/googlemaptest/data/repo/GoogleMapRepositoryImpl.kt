package com.example.googlemaptest.data.repo

import com.example.googlemaptest.api.GooglePlaceService
import com.example.googlemaptest.data.model.NearbySearchResponse
import com.example.googlemaptest.data.model.place.PlaceInfoResponse
import com.example.googlemaptest.room.allergie.AllergieDao
import com.example.googlemaptest.room.allergie.AllergieEntity
import com.example.googlemaptest.room.bookmark.BookmarkDao
import com.example.googlemaptest.room.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class GoogleMapRepositoryImpl @Inject constructor(
    private val googleMapService: GooglePlaceService,
    private val bookmarkDao: BookmarkDao,
    private val allergyDao: AllergieDao
) :
    GoogleMapRepository {

    override val allergyList: Flow<List<AllergieEntity>>
        get() = allergyDao.getAll()

    override val bookmarkList: Flow<List<BookmarkEntity>>
        get() = bookmarkDao.getAll()

    override suspend fun nearbySearch(
        keyword: String,
        location: String
    ): Response<NearbySearchResponse> =
        googleMapService.getNearbySearch(keyword, location)

    override suspend fun getPlaceDetail(placeId: String): Response<PlaceInfoResponse> =
        googleMapService.getPlaceDetail(placeId)

    override suspend fun addBookmark(entity: BookmarkEntity) {
        bookmarkDao.registerBookmark(entity)
    }

    override suspend fun deleteBookmark(entity: BookmarkEntity) {
        bookmarkDao.deleteBookmark(entity)
    }

    override suspend fun addAllergie(name: String) {
        allergyDao.registerAllergie(AllergieEntity(name = name))
    }

    override suspend fun deleteAllergie(allergieEntity: AllergieEntity) {
        allergyDao.deleteAllergie(allergieEntity)
    }
}