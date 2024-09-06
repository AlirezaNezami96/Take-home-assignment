package com.affirm.takehome.network.yelp

import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.places.PlacesResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YelpResponse(
    @SerialName("businesses") val restaurants: List<YelpRestaurant> = listOf()
)

fun YelpResponse.toDomainModel(): List<Restaurant> {
    return this.restaurants.map {
        Restaurant(
            id = it.id,
            name = it.name,
            image = it.image,
            rating = it.rating
        )
    }
}