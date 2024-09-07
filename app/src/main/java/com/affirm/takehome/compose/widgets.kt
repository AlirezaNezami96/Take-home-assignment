package com.affirm.takehome.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.affirm.takehome.R

@Composable
fun IconTextButton(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier,
    counter: Int = 0
) {
    Button(
        modifier = modifier,
        onClick = { onClick() }) {
        Row {
            if (counter != 0) {
                Text(
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.White,
                    text = counter.toString()
                )
            }
            Image(
                modifier = Modifier.weight(1f),
                painter = painterResource(id = iconRes),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "content"
            )

        }
    }
}

@Composable
fun RestaurantCard(
    name: String,
    image: String
) {
    Card(
        shape = RoundedCornerShape(8),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Surface {
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                placeholder = painterResource(R.drawable.img_placeholder),
                model = Builder(LocalContext.current)
                    .data(image)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "description"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                Alignment.BottomEnd
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

