package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T: Any> CustomHorizontalPagerForPaging(
    pagerContent: LazyPagingItems<T>,
    pagerState: PagerState,
    key: ((item: T) -> Any),
    content: @Composable (T) -> Unit,
    // Custom options
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSpacing: Dp = Dp.Unspecified,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = contentPadding,
        pageSpacing = pageSpacing,
        verticalAlignment = verticalAlignment,
        key = { key(pagerContent[it]!!) }
    ) { index ->
        val pagerIndex = pagerContent[index]

        if (pagerIndex != null) {
            content(pagerIndex)
        }

        pagerContent.loadState.let { loadStates ->
            when {
                loadStates.refresh is LoadState.NotLoading && pagerContent.itemCount < 1 -> {
                    // No data available
                }
                loadStates.refresh is LoadState.Error -> {
                    /**
                     * when ((loadStates.refresh as LoadState.Error).error) {
                     * is HttpException -> { stringResource(R.string.something_wrong) }
                     * is IOException -> { stringResource(R.string.internet_problem) }
                     * else -> { stringResource(R.string.unknown_error) }
                     */
                }
                loadStates.refresh is LoadState.Loading -> {
                    CircularProgressIndicator()
                }
                loadStates.append is LoadState.Loading -> {
                    CircularProgressIndicator()
                }
                loadStates.append is LoadState.Error -> {
                    // An error occurred
                }
            }
        }
    }
}