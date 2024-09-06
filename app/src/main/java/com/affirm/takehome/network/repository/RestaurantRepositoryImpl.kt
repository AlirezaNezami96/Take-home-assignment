package com.affirm.takehome.network.repository

import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.api.ApiHelper
import com.affirm.takehome.network.api.ApiType
import com.affirm.takehome.network.helper.ResultState
import com.affirm.takehome.network.places.PlacesRestaurantApi
import com.affirm.takehome.network.places.toDomainModel
import com.affirm.takehome.network.yelp.YelpRestaurantApi
import com.affirm.takehome.network.yelp.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val MAX_OFFSET = 20

class RestaurantRepositoryImpl(
    private val yelpApi: YelpRestaurantApi,
    private val placesApi: PlacesRestaurantApi,
    private val apiHelper: ApiHelper
) : RestaurantRepository {

    override suspend fun fetchRestaurants(
        latitude: Double,
        longitude: Double
    ): Flow<ResultState<List<Restaurant>>> = flow {
        try {
            // Determine which API to call
            val apiType = apiHelper.getNextApi()

            // Fetch data from the appropriate API
            val restaurants: List<Restaurant> = when (apiType) {
                ApiType.Yelp -> {
                    val response =
                        yelpApi.getRestaurants(latitude, longitude, apiHelper.yelpPage * MAX_OFFSET)
                    response.toDomainModel()
                }

                ApiType.Places -> {
                    val response =
                        placesApi.getRestaurants(latitude, longitude, apiHelper.nextPageToken)
                    // Update the nextPageToken for future requests
                    if (response.nextPageToken.isNotEmpty()) {
                        apiHelper.updateNextPageToken(response.nextPageToken)
                    }
                    response.toDomainModel()
                }
            }

            // Emit success result with the list of restaurants
            emit(ResultState.Success(restaurants))

        } catch (e: Exception) {
            // Return failure state with the error
            emit(ResultState.Failure(e))
        }
    }
}
