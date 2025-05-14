package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T: Any> CustomLazyRow(
    itemsContent: List<T>,
    key: ((item: T) -> Any),
    // Custom options
    lazyState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable (T) -> Unit
) {
    LazyRow(
        state = lazyState,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
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