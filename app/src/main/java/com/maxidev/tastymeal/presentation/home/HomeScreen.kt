@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.home

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomHorizontalPagerForPaging
import com.maxidev.tastymeal.presentation.components.CustomLazyRow
import com.maxidev.tastymeal.presentation.search.CardMealItem
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme
import com.maxidev.tastymeal.utils.Resource
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClick: (String) -> Unit
) {
    //homeState.collectAsStateWithLifecycle()
    val randomState by viewModel.randomFlow.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categoriesFlow.collectAsStateWithLifecycle()
    val searchByLetter by viewModel.searchByLetterState.collectAsStateWithLifecycle()
    //val filterCountryState by viewModel.filterByCountryFlow.collectAsStateWithLifecycle()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    HomeScreenContent(
        homeState = randomState,
        categories = categoriesState.categories,
        searchByLetter = searchByLetter,
        //filterByCountry = filterCountryState.filterByCountry,
        scrollBehavior = scrollBehavior,
        onClick = onClick
    )
}

/* ---------- Content screen ---------- */

@Composable
private fun HomeScreenContent(
    homeState: Resource<MinimalMeal>,
    categories: List<CategoriesMeal>,
    searchByLetter: HomeUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: (String) -> Unit
) {
    val pagingItems = searchByLetter.searchByLetter.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pagingItems.itemCount })
    val pageInteractionSource = remember { MutableInteractionSource() }
    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val pageIsPressed by pageInteractionSource.collectIsPressedAsState()
    val autoAdvance = !pagerIsDragged && !pageIsPressed

    if (autoAdvance) {
        LaunchedEffect(pagerState, pageInteractionSource) {
            while (true) {
                delay(2000)

                // TODO: FIX Arithmetic exception
                val nextPage = (pagerState.currentPage + 1) % pagingItems.itemCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomCenteredTopBar(
                title = {
                    Text(text = "Tasty Meal")
                },
                navigationIcon = { /* TODO: Navigation drawer ? */ },
                actions = { /* TODO: Actions ? */ },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            //val pagingItems = searchByLetter.searchByLetter.collectAsLazyPagingItems()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = rememberLazyListState(), // Make it reusable
                //contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Recommended
                item {
                    CustomHorizontalPagerForPaging(
                        modifier = Modifier
                            .clickable(
                                interactionSource = pageInteractionSource,
                                indication = LocalIndication.current
                            ) {
                                // TODO: Navigate to detail.
                            },
                        pagerContent = pagingItems,
                        pagerState = pagerState,
                        key = { it.idMeal },
                        contentPadding = PaddingValues(10.dp),
                        pageSpacing = 4.dp,
                        content = {
                            RecommendationsItem(model = it)
                        }
                    )
                }

                // Categories
                item {
                    Column {
                        TitleHeader(title = "Categories")
                        CustomLazyRow(
                            itemsContent = categories,
                            key = { keys -> keys.idMeal },
                            verticalAlignment = Alignment.CenterVertically,
                            content = {
                                CategoryItem(
                                    model = it
                                )
                            }
                        )
                    }
                }

                // Random meal
                item {
                    Column {
                        TitleHeader(title = "Recommended meal")
                        when (homeState) {
                            is Resource.Error<*> -> {
                                Text(text = homeState.message ?: "Error")
                            }

                            is Resource.Loading<*> -> {
                                CircularProgressIndicator()
                            }

                            is Resource.Success<*> -> {
                                CardMealItem(
                                    content = homeState.data ?: return@item,
                                    onClick = { onClick(homeState.data.idMeal) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------- Title header ---------- */

@Composable
private fun TitleHeader(
    title: String
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
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
    TastyMealTheme {
        TitleHeader(title = "Categories")
    }
}

/* ---------- Categories item ---------- */

@Composable
private fun CategoryItem(
    model: CategoriesMeal
) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        CustomAsyncImage(
            model = model.strThumb,
            contentDescription = model.strName,
            contentScale = ContentScale.Crop,
            height = 100.dp,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = model.strName,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    TastyMealTheme {
        CategoryItem(
            model = CategoriesMeal(
                idMeal = "1",
                strName = "Beef",
                strThumb = "image"
            )
        )
    }
}

/* ---------- Filter item ---------- */

@Composable
private fun RecommendationsItem(
    model: MinimalMeal
) {
    Box {
        CustomAsyncImage(
            model = model.strMealThumb,
            contentDescription = model.strMeal,
            contentScale = ContentScale.Crop,
            height = 260.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        )
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = model.strMeal,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecommendationsItemPreview() {
    TastyMealTheme {
        RecommendationsItem(
            model = MinimalMeal(
                idMeal = "",
                strMeal = "Lorem impsum",
                strMealThumb = "image",
                strCategory = "Beef"
            )
        )
    }
}