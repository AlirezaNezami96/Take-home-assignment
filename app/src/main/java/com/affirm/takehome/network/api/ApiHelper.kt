package com.affirm.takehome.network.api

class ApiHelper {
    private var currentApi: ApiType = ApiType.YELP
    private var yelpPage = 1
    private var placesPage = 1

    // Enum to keep track of the current API
    enum class ApiType {
        YELP,
        PLACES
    }

    // Function to get the next API call and page
    fun getNextApi(): Pair<ApiType, Int> {
        return if (currentApi == ApiType.YELP) {
            currentApi = ApiType.PLACES
            Pair(ApiType.YELP, yelpPage++)
        } else {
            currentApi = ApiType.YELP
            Pair(ApiType.PLACES, placesPage++)
        }
    }
}
