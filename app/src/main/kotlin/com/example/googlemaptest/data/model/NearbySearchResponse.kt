package com.example.googlemaptest.data.model

data class NearbySearchResponse(
    val results: List<Result>,
    val status: String // Ok or Else
)