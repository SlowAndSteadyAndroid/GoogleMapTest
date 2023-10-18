package com.example.googlemaptest.models

class Place(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
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