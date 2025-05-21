@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.tastymeal.domain.model.Meal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomIconButton
import com.maxidev.tastymeal.presentation.components.CustomLazyColumn
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    navigateToDetailOffline: (String) -> Unit
) {
    val state by viewModel.getBookmarks.collectAsStateWithLifecycle()

    BookmarkContent(
        allBookmarks = state.allBookmarks,
        onClearAll = { viewModel.clearAllBookmarks() },
        navigateToDetailOffline = navigateToDetailOffline
    )
}

@Composable
private fun BookmarkContent(
    allBookmarks: List<Meal>,
    onClearAll: () -> Unit,
    navigateToDetailOffline: (String) -> Unit
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomCenteredTopBar(
                title = {
                    Text(text = "Bookmarks")
                },
                actions = {
                    // TODO: Add alert dialog and snackbar.
                    CustomIconButton(
                        imageVector = Icons.Rounded.DeleteSweep,
                        contentDescription = "Clear all bookmarks",
                        onClick = onClearAll
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            CustomLazyColumn(
                modifier = Modifier.fillMaxSize(),
                itemsContent = allBookmarks,
                key = { it.idMeal },
                lazyState = rememberLazyListState(),
                content = {
                    BookmarkedItem(
                        strMealThumb = it.strMealThumb,
                        strMeal = it.strMeal,
                        strCategory = it.strCategory,
                        onClick = { navigateToDetailOffline(it.idMeal) }
                    )
                }
            )
        }
    }
}

/* ----- Bookmark item ----- */
@Composable
fun BookmarkedItem(
    strMealThumb: String,
    strMeal: String,
    strCategory: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomAsyncImage(
                model = strMealThumb,
                contentDescription = strMeal,
                height = 100.dp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = strMeal,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
                Text(
                    text = strCategory,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun BookmarkedItemPreview() {
    TastyMealTheme {
        BookmarkedItem(
            strMealThumb = "image", strMeal = "Salad with chicken", strCategory = "Beef",
            onClick = {}
        )
    }
}