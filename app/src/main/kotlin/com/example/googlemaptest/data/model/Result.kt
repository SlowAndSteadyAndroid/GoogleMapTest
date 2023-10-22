package com.example.googlemaptest.data.model

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

data class Result(
    val geometry: Geometry,
    val name: String,
    val place_id: String,
)


fun Result.toMarkerOption(): MarkerOptions =
    MarkerOptions().apply {
        position(geometry.location.toLatLng())
        title(name)
    }

