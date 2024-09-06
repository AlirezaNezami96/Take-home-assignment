package com.affirm.takehome

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.helper.ResultState
import com.affirm.takehome.network.repository.RestaurantRepositoryImpl
import kotlinx.coroutines.launch

//TODO: fetch restaurants based on location
class MainViewModel(
    private val restaurantRepository: RestaurantRepositoryImpl
) : ViewModel() {

    private var currentLocation: Location? = null

    private var state by mutableStateOf(RestaurantUIState())

    fun getRestaurant(index: Int): Restaurant = state.restaurants[index]

    fun isLoading(): Boolean = state.isLoading

    fun getError(): String? = state.errorMessage

    fun getSize(): Int = state.restaurants.size

    fun getLikes(): Int = this.state.likes

    fun getDislikes(): Int = this.state.dislikes

    private fun updateList(data: List<Restaurant>) {
        val currentSet = state.restaurants.toMutableList()
        currentSet.addAll(data)
        this.state = state.copy(
            restaurants = currentSet,
            isLoading = false,
            errorMessage = null
        )
    }

    fun setLoading(isLoading: Boolean) {
        this.state = state.copy(
            isLoading = isLoading,
            errorMessage = null
        )
    }

    fun setErrorMessage(error: String?) {
        this.state = state.copy(
            isLoading = false,
            errorMessage = error
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
            is ResultState.Failure -> {
                setErrorMessage(locationResult.error.message)
            }
            ResultState.Loading -> {
                setLoading(true)
            }

            is ResultState.Success -> {
                currentLocation = locationResult.data
                fetchRestaurants(locationResult.data)
            }
        }
    }

    fun fetchRestaurants(location: Location) {
        viewModelScope.launch {
            restaurantRepository.fetchRestaurants(location.latitude, location.longitude)
                .collect { result ->
                    when (result) {
                        is ResultState.Failure -> {
                            setErrorMessage(result.error.message)
                        }
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

    fun fetchNextPage() {
        currentLocation?.let { fetchRestaurants(it) }
    }

}

data class RestaurantUIState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val likes: Int = 0,
    val dislikes: Int = 0
)
