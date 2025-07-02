@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maxidev.tastymeal.R
import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit,
    navigateToFilter: (String) -> Unit
) {
    val randomState by viewModel.randomFlow.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categoriesFlow.collectAsStateWithLifecycle()
    val searchByLetter by viewModel.searchByLetterState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    HomeScreenContent(
        isRefreshing = isRefreshing,
        homeState = randomState,
        categories = categoriesState,
        searchByLetter = searchByLetter,
        navigateToDetail = navigateToDetail,
        navigateToFilter = navigateToFilter,
        pullToRefresh = viewModel::refreshAll
    )
}

/* ---------- Content screen ---------- */

@Composable
private fun HomeScreenContent(
    isRefreshing: Boolean,
    homeState: Resource<MinimalMeal>,
    categories: List<CategoriesMeal>,
    searchByLetter: Flow<PagingData<MinimalMeal>>,
    navigateToDetail: (String) -> Unit,
    navigateToFilter: (String) -> Unit,
    pullToRefresh: () -> Unit
) {
    val pagingItems = searchByLetter.collectAsLazyPagingItems()

    Scaffold { innerPadding ->
        val pullToRefreshState = rememberPullToRefreshState()
        val verticalScroll = rememberScrollState()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = pullToRefresh,
            state = pullToRefreshState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .verticalScroll(verticalScroll),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val spacing = 16.dp

                HeaderItem()

                // Recommended
                TitleHeader(
                    modifier = Modifier.align(Alignment.Start),
                    title = "Recommendations"
                )
                LazyRow(
                    state = rememberLazyListState(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.idMeal }
                    ) { index ->
                        val pagingContent = pagingItems[index]

                        if (pagingContent != null) {
                            RecommendationsItem(
                                model = pagingContent,
                                onClick = navigateToDetail
                            )
                        }
                    }

                    pagingItems.loadState.let { loadStates ->
                        when {
                            loadStates.refresh is LoadState.NotLoading && pagingItems.itemCount < 1 -> {
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

                // Random meal
                TitleHeader(
                    modifier = Modifier.align(Alignment.Start),
                    title = "Something delicious for you"
                )
                when (homeState) {
                    is Resource.Error<*> -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                text = stringResource(R.string.internet_problem),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    is Resource.Loading<*> -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            LinearProgressIndicator()
                        }
                    }

                    is Resource.Success<*> -> {
                        RandomRecipeItem(
                            model = homeState.data ?: return@Column,
                            onClick = { navigateToDetail(homeState.data.idMeal) }
                        )
                    }
                }

                // Categories
                TitleHeader(
                    modifier = Modifier.align(Alignment.Start),
                    title = "Categories"
                )
                LazyRow(
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(start = spacing, end = spacing, bottom = spacing),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    items(
                        items = categories,
                        key = { it.idMeal }
                    ) {
                        CategoryItem(
                            model = it,
                            onClick = navigateToFilter
                        )
                    }
                }
            }
        }
    }
}

/* ---------- Headers ---------- */

@Composable
private fun HeaderItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "What would you like to cook today?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview
@Composable
private fun HeadersItemPreview() {
    HeaderItem()
}

@Composable
private fun TitleHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .then(modifier)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
private fun TitleHeaderPreview() {
    TitleHeader(title = "Categories")
}

/* ---------- Categories item ---------- */

@Composable
private fun CategoryItem(
    model: CategoriesMeal,
    onClick: (String) -> Unit
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = { onClick(model.strName) }
    ) {
        CustomAsyncImage(
            model = model.strThumb,
            contentDescription = model.strName,
            contentScale = ContentScale.Crop,
            onClick = { onClick(model.strName) },
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = model.strName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    CategoryItem(
        model = CategoriesMeal(
            idMeal = "1",
            strName = "Beef",
            strThumb = "image"
        ),
        onClick = {}
    )
}

/* ---------- Recommendations item ---------- */

@Composable
private fun RecommendationsItem(
    model: MinimalMeal,
    onClick: (String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.outlinedCardElevation(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        CustomAsyncImage(
            onClick = { onClick(model.idMeal) },
            model = model.strMealThumb,
            contentDescription = model.strMeal,
            contentScale = ContentScale.Fit,
            height = 200.dp,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = model.strMeal,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(bottom = 8.dp, start = 10.dp, end = 10.dp)
                .width(180.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RecommendationsItemPreview() {
    RecommendationsItem(
        model = MinimalMeal(
            idMeal = "",
            strMeal = "Lorem impsum",
            strMealThumb = "image",
            strCategory = "Beef"
        ),
        onClick = {}
    )
}

/* ---------- Random recipe ---------- */

@Composable
private fun RandomRecipeItem(
    model: MinimalMeal,
    onClick: (String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.outlinedCardElevation(8.dp)
    ) {
        CustomAsyncImage(
            onClick = { onClick(model.idMeal) },
            model = model.strMealThumb,
            contentDescription = model.strMeal,
            contentScale = ContentScale.Crop,
            height = 170.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = model.strMeal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Preview
@Composable
private fun RandomRecipeItemPreview() {
    RandomRecipeItem(
        model = MinimalMeal(
            strMeal = "Beef asado",
            strMealThumb = "image",
            strCategory = "Beef",
            idMeal = "0"
        ),
        onClick = {}
    )
}