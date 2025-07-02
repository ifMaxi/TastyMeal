package com.maxidev.tastymeal.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maxidev.tastymeal.R
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomSearchBar
import retrofit2.HttpException
import java.io.IOException

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onClick: (String) -> Unit
) {
    val state by viewModel.searchState.collectAsStateWithLifecycle()
    val query by viewModel.input

    SearchScreenContent(
        query = query,
        onQueryChange = viewModel::onInputChange,
        onSearch = viewModel::searchMeals,
        paging = state,
        onClick = onClick
    )
}

@Composable
private fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    paging: SearchUiState,
    onClick: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            CustomSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                content = { /* Do nothing. */ }
            )
        }
    ) { innerPadding ->
        val pagingState = paging.search.collectAsLazyPagingItems()
        val lazyState = rememberLazyListState()

        if (pagingState.itemCount == 0) {
            SearchStandByItem()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .consumeWindowInsets(innerPadding),
                state = lazyState,
                contentPadding = innerPadding,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    count = pagingState.itemCount,
                    key = pagingState.itemKey { it.idMeal }
                ) { index ->
                    val pagingContent = pagingState[index]

                    if (pagingContent != null) {
                        CardMealItem(
                            content = pagingContent,
                            onClick = onClick
                        )
                    }
                }

                pagingState.loadState.let { loadStates ->
                    when {
                        loadStates.refresh is LoadState.NotLoading && pagingState.itemCount < 1 -> {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        text = stringResource(R.string.no_data_available),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        loadStates.refresh is LoadState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        text = when ((loadStates.refresh as LoadState.Error).error) {
                                            is HttpException -> { stringResource(R.string.something_wrong) }
                                            is IOException -> { stringResource(R.string.internet_problem) }
                                            else -> { stringResource(R.string.unknown_error) }
                                        },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        loadStates.refresh is LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillParentMaxWidth()) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        loadStates.append is LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillParentMaxWidth()) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        loadStates.append is LoadState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        text = stringResource(R.string.something_wrong),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ----- Search item ----- */

@Composable
private fun CardMealItem(
    content: MinimalMeal,
    onClick: (String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier.padding(horizontal = 10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top)
                .fillMaxWidth()
                .clickable { onClick(content.idMeal) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = content.strMeal,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .widthIn(min = 50.dp, max = 200.dp)
                )
                Text(
                    text = content.strCategory,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
            CustomAsyncImage(
                model = content.strMealThumb,
                contentDescription = content.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .size(120.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CardMealItemPreview() {
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

/* ----- Search stand by ----- */

@Composable
private fun SearchStandByItem() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Find something delicious",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.size(20.dp))
        Image(
            painter = painterResource(R.drawable.search_recipe),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
        )
    }
}

@Preview
@Composable
private fun SearchStandByItemPreview() {
    SearchStandByItem()
}