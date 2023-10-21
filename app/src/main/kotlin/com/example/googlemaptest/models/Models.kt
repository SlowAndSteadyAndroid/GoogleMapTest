package com.example.googlemaptest.models


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class Place(
    @JsonProperty(value = "id", required = true)
    val id: String,
    @JsonProperty(value = "name", required = true)
    val name: String,
    @JsonProperty(value = "latitude", required = true)
    val latitude: Double,
    @JsonProperty(value = "longitude", required = true)
    val longitude: Double
    ,@JsonProperty(value = "description", required = true)
    val description: String = ""
)

fun List<Place>.search(keyword: String): List<Place> {

    if (keyword.trim().isEmpty())
        return this

    val convertList = mutableListOf<Place>()


    for (place in this) {

        val descriptionList = place.description.lowercase().split(" ")

        for (string in descriptionList) {
            if (string.lowercase() == keyword.lowercase().trim()) {
                convertList.add(place)
                break
            }

            if (!string.chars().allMatch(Character::isAlphabetic)) {

                var convertString = ""

                string.toCharArray().forEach { c ->
                    if (Character.isAlphabetic(c.code)) {
                        convertString += c
                    } else {
                        convertString += if (c == '.' || c == ',' || c == '!' || c == '?' || c == ';' || c == ':'
                            || c == '/'
                        ) {
                            " "
                        } else {
                            ""
                        }
                    }
                }

                convertString.split(" ").forEach {word ->
                    if (word.lowercase() == keyword.lowercase().trim()) {
                        convertList.add(place)
                        return@forEach
                    }
                }
            }
        }

    }

    return convertList
}