package com.example.googlemaptest.models


class ResultMightThrow<T> {
    // Allow retrieving the exception
    val exception: Exception?

    // Stored separately to allow the result to be nullable
    private val actualResult: T?

    // Synthetic field with getter
    val result: T
        get() {
            if (exception != null) {
                throw exception
            }
            // Sanity check, since this should never happen
            check(actualResult != null)
            return actualResult
        }

    // Constructor used on success
    // Unusually for Kotlin, we don't provide a primary constructor for this class
    // You can, but it's more awkward than using two secondary constructors
    constructor(setResult: T) {
        actualResult = setResult
        exception = null
    }

    // Constructor used on failure
    constructor(setException: Exception?) {
        actualResult = null
        exception = setException
    }
}
