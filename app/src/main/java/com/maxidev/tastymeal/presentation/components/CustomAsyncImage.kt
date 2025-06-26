package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.SubcomposeAsyncImage
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    model: String,
    // Custom options
    contentDescription: String? = "",
    contentScale: ContentScale = ContentScale.Fit,
    height: Dp = Dp.Unspecified,
    onClick: () -> Unit = {}
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        loading = { ShimmerImageLoading() },
        modifier = modifier
            .height(height)
            .clickable { onClick() }
    )
}

@Composable
private fun ShimmerImageLoading() {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Box(
        modifier = Modifier
            .shimmer(customShimmer = shimmerInstance)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground)
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ShimmerImageLoafingPreview() {
    ShimmerImageLoading()
}