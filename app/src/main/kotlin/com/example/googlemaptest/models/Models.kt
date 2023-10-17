package com.example.googlemaptest.models

class Place(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String = ""
)