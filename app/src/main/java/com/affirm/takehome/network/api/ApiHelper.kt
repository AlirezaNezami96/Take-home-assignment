package com.affirm.takehome.network.api

class ApiHelper {
    private var currentApiType = ApiType.Places // Store the current type

    var yelpPage: Int = 0
        private set // Only allow updating internally

    var nextPageToken: String = ""
        private set // Only allow updating internally

    private var isPlacesApiExhausted: Boolean = false // Check if the Places API has reached to the end

    // Function to get the next API and handle paging logic
    fun getNextApi(): ApiType {
        return if (isPlacesApiExhausted || currentApiType == ApiType.Places) {
            // Call Yelp API if Places API is exhausted or alternate logic dictates
            yelpPage++
            currentApiType = ApiType.Yelp
            ApiType.Yelp
        } else {
            // Call Google Places API
            currentApiType = ApiType.Places
            ApiType.Places
        }
    }

    // Update the nextPageToken when a response is received from Google Places API
    // If nextPageToken is empty, mark Places API as exhausted
    fun updateNextPageToken(token: String) {
        if (token.isEmpty()) {
            isPlacesApiExhausted = true
        } else {
            nextPageToken = token
        }
    }
}

// Enum to represent which API to call
enum class ApiType {
    Yelp, Places
}
