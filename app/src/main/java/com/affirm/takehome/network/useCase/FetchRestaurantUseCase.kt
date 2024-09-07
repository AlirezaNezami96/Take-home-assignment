package com.affirm.takehome.network.useCase

import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.helper.ResultState
import com.affirm.takehome.network.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchRestaurantUseCase @Inject constructor(private val restaurantRepository: RestaurantRepository) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Flow<ResultState<List<Restaurant>>> =
        restaurantRepository.fetchRestaurants(latitude, longitude)
}