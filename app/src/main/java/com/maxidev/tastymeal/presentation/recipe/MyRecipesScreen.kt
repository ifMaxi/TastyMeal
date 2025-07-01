@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomFab
import com.maxidev.tastymeal.presentation.settings.HeaderTitleItem
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme

@Composable
fun MyRecipesScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    navigateToCreate: () -> Unit,
    navigateToDetail: (Long) -> Unit
) {
    val state by viewModel.recipes.collectAsStateWithLifecycle()

    ScreenContent(
        recipes = state.recipes,
        navigateToCreate = navigateToCreate,
        navigateToDetail = navigateToDetail
    )
}

@Composable
private fun ScreenContent(
    recipes: List<Recipe>,
    navigateToCreate: () -> Unit,
    navigateToDetail: (Long) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            CustomFab(
                onClick = navigateToCreate,
                icon = {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "Create recipe")
                },
                shape = RoundedCornerShape(10.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            )
        }
    ) { innerPadding ->
        val lazyState = rememberLazyStaggeredGridState()

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            columns = StaggeredGridCells.Adaptive(150.dp),
            state = lazyState,
            contentPadding = innerPadding
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                HeaderTitleItem(title = "My recipes")
            }

            items(
                items = recipes,
                key = { it.id }
            ) { index ->
                RecipeItem(
                    title = index.title,
                    imageUri = index.image,
                    cameraUri = index.cameraImage,
                    onClick = { navigateToDetail(index.id) }
                )
            }
        }
    }
}

@Composable
private fun RecipeItem(
    cameraUri: Uri?,
    imageUri: Uri?,
    title: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.outlinedCardElevation(6.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        val photo = if (imageUri == Uri.EMPTY) cameraUri else imageUri

        if (photo.toString().isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = photo),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
        if (title.isNotEmpty()) {
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(all = 10.dp)
            )
        }
    }
}

@Preview
@Composable
private fun RecipeItemPreview() {
    TastyMealTheme {
        RecipeItem(
            title = "Lorem impsum",
            imageUri = Uri.EMPTY,
            cameraUri = Uri.EMPTY,
            onClick = {}
        )
    }
}