package com.affirm.takehome.network.places

class PlacesRestaurantApi(private val placesRestaurantService: PlacesRestaurantService) {

    /**
     * Fetches restaurants near a given latitude and longitude. Returns up to 20 results.
     *
     * @param nextPageToken If more than 20 results exist, a previous search will contain a next page
     * token which can be included to fetch the next batch of results.
     */
    suspend fun getRestaurants(
        latitude: Double,
        longitude: Double,
        nextPageToken: String = ""
    ): PlacesResponse {
        return placesRestaurantService.getPlacesRestaurants("$latitude,$longitude", nextPageToken)
    }
}