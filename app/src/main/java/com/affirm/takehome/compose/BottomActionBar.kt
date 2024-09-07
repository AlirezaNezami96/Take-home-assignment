package com.affirm.takehome.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.affirm.takehome.MainViewModel
import com.affirm.takehome.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Contains Action Button for likes/dislikes
 */
@OptIn(ExperimentalFoundationApi::class)
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
