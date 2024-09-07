package com.affirm.takehome.di

import com.affirm.takehome.network.api.ApiHelper
import com.affirm.takehome.network.places.PlacesRestaurantApi
import com.affirm.takehome.network.places.PlacesRestaurantApiFactory
import com.affirm.takehome.network.repository.RestaurantRepository
import com.affirm.takehome.network.repository.RestaurantRepositoryImpl
import com.affirm.takehome.network.yelp.YelpRestaurantApi
import com.affirm.takehome.network.yelp.YelpRestaurantApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides ApiHelper
    @Provides
    @Singleton
    fun provideApiHelper(): ApiHelper {
        return ApiHelper()
    }


    // Provides YelpRestaurantApi
    @Provides
    @Singleton
    fun provideYelpRestaurantApi(): YelpRestaurantApi {
        return YelpRestaurantApiFactory.create()
    }

    // Provides PlacesRestaurantApi
    @Provides
    @Singleton
    fun providePlacesRestaurantApi(): PlacesRestaurantApi {
        return PlacesRestaurantApiFactory.create()
    }

    // Provides RestaurantRepository
    @Provides
    @Singleton
    fun provideRestaurantRepository(
        yelpApi: YelpRestaurantApi,
        placesApi: PlacesRestaurantApi, apiHelper: ApiHelper
    ): RestaurantRepository {
        return RestaurantRepositoryImpl(yelpApi, placesApi, apiHelper)
    }
}
