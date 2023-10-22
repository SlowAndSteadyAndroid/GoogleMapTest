package com.example.googlemaptest.room.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googlemaptest.data.model.place.Result
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "bookmark_table")
data class BookmarkEntity(
    @PrimaryKey val placeId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String,
    @ColumnInfo(name = "address") val address: String,
)

fun BookmarkEntity.toLatLng(): LatLng = LatLng(latitude, longitude)


fun Result.toBookmarkEntity(): BookmarkEntity =
    BookmarkEntity(
        placeId = place_id,
        name = name,
        url = url,
        latitude = geometry.location.lat,
        longitude = geometry.location.lng,
        rating = rating,
        phoneNumber = formatted_phone_number,
        address = formatted_address
    )