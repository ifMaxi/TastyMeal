package com.maxidev.tastymeal.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomSearchBar
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onClick: (String) -> Unit
) {
    val state by viewModel.searchState.collectAsStateWithLifecycle()
    val lazyState = rememberLazyListState()
    val query by viewModel.input

    SearchScreenContent(
        query = query,
        onQueryChange = viewModel::onInputChange,
        onSearch = viewModel::searchMeals,
        paging = state,
        lazyState = lazyState,
        onClick = onClick
    )
}

@Composable
private fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    paging: SearchUiState,
    lazyState: LazyListState = rememberLazyListState(),
    onClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CustomSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val pagingState = paging.search.collectAsLazyPagingItems()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyState,
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    count = pagingState.itemCount
                ) { index ->
                    val item = pagingState[index]

                    if (item != null) {
                        CardMealItem(
                            content = item,
                            onClick = onClick
                        )
                    }
                }

                pagingState.loadState.let { loadType ->
                    when {
                        loadType.refresh is LoadState.NotLoading && pagingState.itemCount < 1 -> {
                            // TODO: Text for no data available
                        }
                        loadType.refresh is LoadState.Error -> {
                            /*
                            * TODO: Handle error
                            *  when ((loadStates.refresh as LoadState.Error).error) {
                                        is HttpException -> { stringResource(R.string.something_wrong) }
                                        is IOException -> { stringResource(R.string.internet_problem) }
                                        else -> { stringResource(R.string.unknown_error) }
                            * */
                        }
                        loadType.append is LoadState.Error -> {
                            // TODO: Handle error "Error occurred"
                        }
                        loadType.append is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardMealItem(
    content: MinimalMeal,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = { onClick(content.idMeal) }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomAsyncImage(
                model = content.strMealThumb,
                contentDescription = content.strMeal,
                height = 100.dp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = content.strMeal,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = content.strCategory,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview
@Composable
private fun CardMealItemPreview() {
    TastyMealTheme {
        CardMealItem(
            content = MinimalMeal(
                strMeal = "Beef asado",
                strMealThumb = "image",
                strCategory = "Beef",
                idMeal = "0"
            ),
            onClick = {}
        )
    }
}