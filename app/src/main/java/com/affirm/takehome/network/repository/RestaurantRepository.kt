package com.affirm.takehome.network.repository

import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.helper.ResultState
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    suspend fun fetchRestaurants(
        latitude: Double,
        longitude: Double
    ): Flow<ResultState<List<Restaurant>>>
}