@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.tastymeal.R
import com.maxidev.tastymeal.domain.model.FilterByCategory
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomIconButton
import com.maxidev.tastymeal.presentation.detail.LoadingStateItem
import com.maxidev.tastymeal.utils.Resource

@Composable
fun FilterByCategoryScreen(
    viewModel: FilterViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.categoryState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        navigateBack = navigateBack,
        navigateToDetail = navigateToDetail
    )
}

@Composable
private fun ScreenContent(
    state: Resource<List<FilterByCategory>>,
    navigateBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomCenteredTopBar(
                navigationIcon = {
                    CustomIconButton(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Navigate back",
                        onClick = navigateBack
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        val lazyStaggeredState = rememberLazyStaggeredGridState()

        when (state) {
            is Resource.Error<*> -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
            is Resource.Loading<*> -> { LoadingStateItem() }
            is Resource.Success<*> -> {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .consumeWindowInsets(innerPadding),
                    columns = StaggeredGridCells.Adaptive(140.dp),
                    contentPadding = innerPadding,
                    state = lazyStaggeredState,
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                    items(
                        items = state.data ?: return@LazyVerticalStaggeredGrid,
                        key = { it.idMeal }
                    ) { index ->
                        FilteredItem(
                            mealId = index.idMeal,
                            strMeal = index.strMeal,
                            strMealThumb = index.strMealThumb,
                            navigateToDetail = navigateToDetail
                        )
                    }
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

/* ----- Filtered item ----- */

@Composable
private fun FilteredItem(
    mealId: String,
    strMeal: String,
    strMealThumb: String,
    navigateToDetail: (String) -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.outlinedCardElevation(8.dp)
    ) {
        CustomAsyncImage(
            onClick = { navigateToDetail(mealId) },
            model = strMealThumb,
            contentDescription = strMeal,
            contentScale = ContentScale.Crop,
            height = 220.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = strMeal,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilteredItemPreview() {
    FilteredItem(
        strMeal = "Pizza",
        strMealThumb = "",
        mealId = "",
        navigateToDetail = {}
    )
}