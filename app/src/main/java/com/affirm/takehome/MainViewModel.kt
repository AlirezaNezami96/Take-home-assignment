package com.affirm.takehome

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.helper.ResultState
import com.affirm.takehome.network.repository.RestaurantRepository
import kotlinx.coroutines.launch

//TODO: fetch restaurants based on location
class MainViewModel(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private var state by mutableStateOf(RestaurantUIState())

    fun getRestaurant(index: Int): Restaurant = state.restaurants[index]

    fun isLoading(): Boolean = state.isLoading

    fun getSize(): Int = state.restaurants.size

    fun getLikes(): Int = this.state.likes

    fun getDislikes(): Int = this.state.dislikes

    private fun updateList(data: List<Restaurant>) {
        val currentSet = state.restaurants.toMutableList()
        currentSet.addAll(data)
        this.state = state.copy(
            restaurants = currentSet,
            isLoading = false
        )
    }

    fun setLoading(isLoading: Boolean) {
        this.state = state.copy(
            isLoading = isLoading
        )
    }

    fun updateLikes() {
        this.state = this.state.copy(
            likes = state.likes + 1
        )
    }

    fun updateDislikes() {
        this.state = this.state.copy(
            dislikes = state.dislikes + 1
        )
    }

    fun setLocation(locationResult: ResultState<Location>) {
        when (locationResult) {
            is ResultState.Failure -> TODO()
            ResultState.Loading -> {
                setLoading(true)
            }

            is ResultState.Success -> {
                fetchRestaurants(locationResult.data)
            }
        }
    }

    private fun fetchRestaurants(location: Location) {
        viewModelScope.launch {
            restaurantRepository.fetchRestaurants(location.latitude, location.longitude)
                .collect { result ->
                    when (result) {
                        is ResultState.Failure -> TODO()
                        ResultState.Loading -> {
                            setLoading(true)
                        }

                        is ResultState.Success -> {
                            updateList(result.data)
                        }
                    }
                }
        }
    }

}

data class RestaurantUIState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val likes: Int = 0,
    val dislikes: Int = 0
)
