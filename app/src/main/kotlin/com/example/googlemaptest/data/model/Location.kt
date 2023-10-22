package com.example.googlemaptest.data.model

data class Location(
    val lat: Double,
    val lng: Double
)

fun Location.toLatLng() = com.google.android.gms.maps.model.LatLng(lat, lng)