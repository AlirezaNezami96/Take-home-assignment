package com.affirm.takehome

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.affirm.takehome.compose.MainScreen
import com.affirm.takehome.network.helper.ResultState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val LOCATION_PERMISSION_CODE = 101

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            this
        )

        setContent {
            MainScreen(viewModel)
        }

        checkAndRequestPermissionsForLocation()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                getLocationAndFetchRestaurants()
            } else {
                Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkAndRequestPermissionsForLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {
            getLocationAndFetchRestaurants()
        }
    }

    private fun getLocationAndFetchRestaurants() {
        lifecycleScope.launch {
            getLocation().collect { locationResult ->
                viewModel.setLocation(locationResult)
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Flow<ResultState<Location>> = flow {
        emit(ResultState.Loading)

        val location = getLastKnownLocation()

        if (location != null) {
            emit(ResultState.Success(location))
        } else {
            // Request updates if no location available
            try {
                val newLocation = requestLocationUpdates()
                emit(ResultState.Success(newLocation))
            } catch (e: Exception) {
                emit(ResultState.Failure(e))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (!continuation.isCancelled) {
                        continuation.resume(location)
                    }
                }
                .addOnFailureListener { exception ->
                    if (!continuation.isCancelled) {
                        continuation.resumeWithException(exception)
                    }
                }
        }

    @SuppressLint("MissingPermission")
    private suspend fun requestLocationUpdates(): Location =
        suspendCancellableCoroutine { continuation ->
            val locationRequest = LocationRequest.create()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.locations.lastOrNull()
                    if (location != null && !continuation.isCancelled) {
                        continuation.resume(location)
                    } else if (!continuation.isCancelled) {
                        continuation.resumeWithException(Throwable("Location update failed"))
                    }
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            // Cancel the location updates if the coroutine is cancelled
            continuation.invokeOnCancellation {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

}
