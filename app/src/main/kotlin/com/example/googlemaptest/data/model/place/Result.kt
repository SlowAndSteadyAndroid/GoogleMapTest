package com.example.googlemaptest.data.model.place

data class Result(
    val dine_in: Boolean,
    val formatted_address: String,
    val formatted_phone_number: String,
    val geometry: Geometry,
    val name: String,
    val place_id: String,
    val rating: Double,
    val url: String,
)