package com.affirm.takehome

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.affirm.takehome.data.Restaurant
import com.affirm.takehome.network.places.PlacesRestaurantApi
import com.affirm.takehome.network.yelp.YelpRestaurantApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import io.reactivex.Single

//TODO: fetch restaurants based on location
class MainViewModel(
  private val yelpRestaurantApi: YelpRestaurantApi,
  private val placesRestaurantApi: PlacesRestaurantApi,
  private val fusedLocationProviderClient: FusedLocationProviderClient

) {

  private var state by mutableStateOf(RestaurantUIState())

  fun getRestaurant(index: Int): Restaurant = state.restaurants[index]

  fun isLoading(): Boolean = state.isLoading

  fun getSize(): Int = state.restaurants.size

  fun getLikes(): Int = this.state.likes

  fun getDislikes(): Int = this.state.dislikes

  private fun updateStateUI(data: List<Restaurant>) {
    val currentSet = state.restaurants.toMutableList()
    currentSet.addAll(data)
    this.state = state.copy(
      restaurants = currentSet
    )
  }

  fun setLoading(isLoading: Boolean) {
    this.state = state.copy(
      isLoading = isLoading
    )
  }

  @SuppressLint("CheckResult", "MissingPermission")
  private fun getLocation(): Single<Location> {
    return Single.create { emitter ->
      fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        if (location == null) {
          fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create(),
            object : LocationCallback() {
              override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.locations.lastOrNull().let { location ->
                  if (location == null) {
                    emitter.onError(Error("Location load fail"))
                    false
                  } else {
                    emitter.onSuccess(location)
                    true
                  }
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
              }
            },
            Looper.getMainLooper()
          )
        } else {
          emitter.onSuccess(location)
        }
      }
    }
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

}

data class RestaurantUIState(
  val restaurants: List<Restaurant> = emptyList(),
  val isLoading: Boolean = false,
  val likes: Int = 0,
  val dislikes: Int = 0
)
