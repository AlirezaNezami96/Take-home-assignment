package com.affirm.takehome.network.yelp

import javax.inject.Inject

class YelpRestaurantApi @Inject constructor(private val yelpRestaurantService: YelpRestaurantService) {

    /**
     * Fetches restaurants near a given latitude and longitude. Returns 20 results at a time.
     *
     * @param offset An integer offset for results. A values of 0 returns the first 20 results.
     * A value of 20 would return results 20-40.
     */
    suspend fun getRestaurants(latitude: Double, longitude: Double, offset: Int): YelpResponse {
        return yelpRestaurantService.getYelpRestaurants(
            latitude = latitude,
            longitude = longitude,
            offset = offset
        )
    }
}