@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomFab
import com.maxidev.tastymeal.presentation.components.CustomLazyColumn
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme

@Composable
fun MyRecipesScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    navigateToCreate: () -> Unit
) {
    val state by viewModel.recipes.collectAsStateWithLifecycle()

    ScreenContent(
        recipes = state.recipes,
        navigateToCreate = navigateToCreate
    )
}

@Composable
private fun ScreenContent(
    recipes: List<Recipe>,
    navigateToCreate: () -> Unit
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        topBar = {
            CustomCenteredTopBar(
                title = {
                    Text(text = "My recipes")
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            CustomFab(
                onClick = navigateToCreate,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Create recipe"
                    )
                },
                shape = RoundedCornerShape(10.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            )
        }
    ) { innerPadding ->
        Box {
            CustomLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                itemsContent = recipes,
                key = { it.id },
                lazyState = rememberLazyListState(),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    RecipeItem(
                        title = it.title,
                        imageUri = it.image,
                        cameraUri = it.cameraImage
                    )
                }
            )
        }
    }
}

@Composable
private fun RecipeItem(
    cameraUri: Uri?,
    imageUri: Uri?,
    title: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        val photo = if (imageUri == Uri.EMPTY) cameraUri else imageUri

        Image(
            painter = rememberAsyncImagePainter(photo),
            contentDescription = title,
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = title
        )
    }
}

@Preview
@Composable
private fun RecipeItemPreview() {
    TastyMealTheme {
        RecipeItem(
            title = "Lorem impsum",
            imageUri = Uri.EMPTY,
            cameraUri = Uri.EMPTY
        )
    }
}