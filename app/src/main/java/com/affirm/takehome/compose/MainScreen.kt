package com.affirm.takehome.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.affirm.takehome.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val pagerState = rememberPagerState(pageCount = {
        viewModel.getSize()
    })
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val totalPages = viewModel.getSize()
            val thresholdPage = (totalPages - 5)
            // Check if the current page exceeds the threshold
            if (page >= thresholdPage) {
                viewModel.fetchNextPage()
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
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
                        RestaurantCard(
                            name = restaurant.name,
                            image = restaurant.image
                        )
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

                LaunchedEffect(viewModel.getError()) {
                    viewModel.getError()?.let { message ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }
    )
}
    