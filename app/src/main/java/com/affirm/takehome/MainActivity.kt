package com.affirm.takehome

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.affirm.takehome.compose.IconTextButton
import com.affirm.takehome.compose.RestaurantCard
import com.affirm.takehome.network.places.PlacesRestaurantApiFactory
import com.affirm.takehome.network.yelp.YelpRestaurantApiFactory
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val LOCATION_PERMISSION_CODE = 101

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
  private val viewModel by lazy {
    MainViewModel(
      YelpRestaurantApiFactory.create(),
      PlacesRestaurantApiFactory.create(),
      LocationServices.getFusedLocationProviderClient(
        this
      )
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val pagerState = rememberPagerState(pageCount = {
        viewModel.getSize()
      })
      val coroutineScope = rememberCoroutineScope()
      LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
          //TODO: Load more restaurants
        }
      }
      MainScreen(viewModel, pagerState, coroutineScope)
    }

    checkAndRequestPermissionsForLocation()
  }

  @Composable
  fun MainScreen(
    viewModel: MainViewModel,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
  ) {
    Surface {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        HorizontalPager(
          state = pagerState,
          userScrollEnabled = false,
          contentPadding = PaddingValues(8.dp),
          modifier = Modifier
            .padding(16.dp)
            .weight(1f)
        ) { page ->
          val restaurant = viewModel.getRestaurant(page)
          RestaurantCard(name = restaurant.name, image = restaurant.image)
        }

        BottomActionBar(
          viewModel = viewModel,
          pagerState = pagerState,
          coroutineScope = coroutineScope
        )

      }

      if (viewModel.isLoading()) {
        LoadingIndicator()
      }
    }
  }


  /**
   * Contains Action Button for likes/dislikes
   */
  @Composable
  fun BottomActionBar(
    viewModel: MainViewModel,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
  ) {
    Row(
      modifier = Modifier
        .padding(all = 8.dp)
        .height(64.dp)
    ) {
      //Dislike Button
      IconTextButton(
        iconRes = R.drawable.thumb_down,
        modifier = Modifier
          .weight(1f)
          .height(48.dp),
        onClick = {
          coroutineScope.launch {
            val currentPage = pagerState.currentPage
            pagerState.scrollToPage(currentPage + 1)
            viewModel.updateDislikes()
          }
        },
        counter = viewModel.getDislikes()
      )

      // Add a horizontal space between the image and the column
      Spacer(modifier = Modifier.width(8.dp))

      //Like Button
      IconTextButton(
        iconRes = R.drawable.thumb_up,
        modifier = Modifier
          .weight(1f)
          .height(48.dp),
        onClick = {
          coroutineScope.launch {
            val currentPage = pagerState.currentPage
            pagerState.scrollToPage(currentPage + 1)
            viewModel.updateLikes()
          }
        },
        counter = viewModel.getLikes()
      )

    }

    //TBA
    Row(
      modifier = Modifier
        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        .height(64.dp)
    ) {
      IconTextButton(
        iconRes = R.drawable.bullet_list,
        modifier = Modifier
          .weight(1f)
          .height(48.dp),
        onClick = {
          //TODO: This is not part of the takehome
        })
    }
  }

  @Composable
  fun LoadingIndicator() {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .background(Color.Transparent)
    ) {
      CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
      )
    }
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
        // TODO
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
      // TODO
    }
  }
}
