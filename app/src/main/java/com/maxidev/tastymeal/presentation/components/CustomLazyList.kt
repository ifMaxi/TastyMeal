package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T: Any> CustomLazyRow(
    itemsContent: List<T>,
    key: ((item: T) -> Any),
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        items(
            items = itemsContent,
            key = key
        ) { index ->
            content(index)
        }
    }
}

@Composable
fun <T: Any> CustomLazyColumn(
    itemsContent: List<T>,
    key: ((item: T) -> Any),
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = lazyState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        items(
            items = itemsContent,
            key = key
        ) { index ->
            content(index)
        }
    }
}

@Composable
fun <T: Any> CustomLazyHorizontalGrid(
    itemsContent: List<T>,
    key: ((item: T) -> Any),
    rows: GridCells,
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit
) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = rows,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items(
            items = itemsContent,
            key = key
        ) { index ->
            content(index)
        }
    }
}

@Composable
fun <T: Any> CustomLazyVerticalGrid(
    itemsContent: List<T>,
    key: ((item: T) -> Any),
    columns: GridCells,
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items(
            items = itemsContent,
            key = key
        ) { index ->
            content(index)
        }
    }
}

@Composable
fun <T: Any> CustomLazyVerticalStaggeredGrid(
    itemsContent: List<T>,
    key: ((item: T) -> Any),
    columns: StaggeredGridCells,
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalItemSpacing: Dp = 0.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
    content: @Composable (T) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = columns,
        state = lazyState,
        contentPadding = contentPadding,
        verticalItemSpacing = verticalItemSpacing,
        horizontalArrangement = horizontalArrangement
    ) {
        items(
            items = itemsContent,
            key = key
        ) { index ->
            content(index)
        }
    }
}

/**
 * Customizable lazy list for paging.
 */
@Composable
fun <T: Any> CustomLazyHorizontalGridPaging(
    itemsContent: LazyPagingItems<T>,
    key: ((item: T) -> Any),
    rows: GridCells,
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit
) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = rows,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items(
            count = itemsContent.itemCount,
            key = { key(itemsContent[it]!!) }
        ) { index ->
            val pagingContent = itemsContent[index]

            if (pagingContent != null) {
                content(pagingContent)
            }
        }
    }
}

@Composable
fun <T: Any> CustomLazyVerticalGridPaging(
    itemsContent: LazyPagingItems<T>,
    key: ((item: T) -> Any),
    columns: GridCells,
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items(
            count = itemsContent.itemCount,
            key = { key(itemsContent[it]!!) }
        ) { index ->
            val pagingContent = itemsContent[index]

            if (pagingContent != null) {
                content(pagingContent)
            }
        }

        itemsContent.loadState.let { loadStates ->
            when {
                loadStates.refresh is LoadState.NotLoading && itemsContent.itemCount < 1 -> {
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
                loadStates.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                loadStates.append is LoadState.Error -> {
                    // An error occurred
                }
            }
        }
    }
}

@Composable
fun <T: Any> CustomLazyRowPaging(
    itemsContent: LazyPagingItems<T>,
    key: ((item: T) -> Any),
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        items(
            count = itemsContent.itemCount,
            key = { key(itemsContent[it]!!) }
        ) { index ->
            val pagingContent = itemsContent[index]

            if (pagingContent != null) {
                content(pagingContent)
            }
        }

        itemsContent.loadState.let { loadStates ->
            when {
                loadStates.refresh is LoadState.NotLoading && itemsContent.itemCount < 1 -> {
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
                    item {
                        CircularProgressIndicator()
                    }
                }
                loadStates.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                loadStates.append is LoadState.Error -> {
                    // An error occurred
                }
            }
        }
    }
}

@Composable
fun <T: Any> CustomLazyColumnPaging(
    itemsContent: LazyPagingItems<T>,
    key: ((item: T) -> Any),
    // Custom options
    modifier: Modifier = Modifier,
    lazyState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = lazyState,
        contentPadding = contentPadding,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        items(
            count = itemsContent.itemCount,
            key = { key(itemsContent[it]!!) }
        ) { index ->
            val pagingContent = itemsContent[index]

            if (pagingContent != null) {
                content(pagingContent)
            }
        }

        itemsContent.loadState.let { loadStates ->
            when {
                loadStates.refresh is LoadState.NotLoading && itemsContent.itemCount < 1 -> {
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
                    item {
                        CircularProgressIndicator()
                    }
                }
                loadStates.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                loadStates.append is LoadState.Error -> {
                    // An error occurred
                }
            }
        }
    }
}