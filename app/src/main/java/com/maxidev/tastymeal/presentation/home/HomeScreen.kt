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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomLazyRow
import com.maxidev.tastymeal.presentation.components.CustomLazyRowPaging
import com.maxidev.tastymeal.utils.Resource

// TODO: Shimmer loading effect in images.

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val randomState by viewModel.randomFlow.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categoriesFlow.collectAsStateWithLifecycle()
    val searchByLetter by viewModel.searchByLetterState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    HomeScreenContent(
        isRefreshing = isRefreshing,
        homeState = randomState,
        categories = categoriesState.categories,
        searchByLetter = searchByLetter,
        navigateToDetail = navigateToDetail,
        pullToRefresh = viewModel::refreshAll
    )
}

/* ---------- Content screen ---------- */

@Composable
private fun HomeScreenContent(
    isRefreshing: Boolean,
    homeState: Resource<MinimalMeal>,
    categories: List<CategoriesMeal>,
    searchByLetter: HomeUiState,
    navigateToDetail: (String) -> Unit,
    pullToRefresh: () -> Unit
) {
    val pagingItems = searchByLetter.searchByLetter.collectAsLazyPagingItems()

    Scaffold { innerPadding ->
        val lazyState = rememberLazyListState()
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = pullToRefresh,
            state = pullToRefreshState
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding),
                contentPadding = innerPadding,
                state = lazyState,
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val spacing = 16.dp

                item { HeaderItem() }

                // Categories
                item {
                    CustomLazyRow(
                        itemsContent = categories,
                        key = { keys -> keys.idMeal },
                        contentPadding = PaddingValues(horizontal = spacing),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        content = {
                            CategoryItem(
                                model = it,
                                onClick = { /* TODO: Navigate to recipe list. */ }
                            )
                        }
                    )
                }

                // Recommended
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                        TitleHeader(title = "Recommendations")
                        CustomLazyRowPaging(
                            modifier = Modifier.fillMaxWidth(),
                            itemsContent = pagingItems,
                            key = { it.idMeal },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            content = {
                                RecommendationsItem(
                                    model = it,
                                    onClick = navigateToDetail
                                )
                            }
                        )
                    }
                }

                // Random meal
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                        TitleHeader(title = "Something delicious for you")
                        when (homeState) {
                            is Resource.Error<*> -> {
                                // TODO: Replace with a animation or a message.
                                Text(text = homeState.message ?: "Error")
                            }

                            is Resource.Loading<*> -> {
                                CircularProgressIndicator()
                            }

                            is Resource.Success<*> -> {
                                RandomRecipeItem(
                                    model = homeState.data ?: return@item,
                                    onClick = { navigateToDetail(homeState.data.idMeal) }
                                )
                            }
                        }
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
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 26.sp,
                textAlign = TextAlign.Start,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.scrim,
                    blurRadius = 1f
                )
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "What would you like to cook today?",
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.scrim,
                    blurRadius = 1f
                )
            ),
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
    title: String
) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
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
        shape = RoundedCornerShape(10.dp)
    ) {
        CustomAsyncImage(
            model = model.strThumb,
            contentDescription = model.strName,
            contentScale = ContentScale.Crop,
            onClick = { onClick(model.idMeal) },
            height = 40.dp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = model.strName,
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 10.dp, vertical = 8.dp)
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
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box {
            CustomAsyncImage(
                onClick = { onClick(model.idMeal) },
                model = model.strMealThumb,
                contentDescription = model.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawWithContent {
                        val colors = listOf(Color.Black, Color.Black, Color.Black, Color.Transparent)

                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(colors = colors),
                            blendMode = BlendMode.DstIn
                        )
                    }
            )
            Text(
                text = model.strMeal,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                ),
                maxLines = 2,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .width(180.dp)
            )
        }
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
        shape = RoundedCornerShape(10.dp)
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
            fontWeight = FontWeight.Medium,
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