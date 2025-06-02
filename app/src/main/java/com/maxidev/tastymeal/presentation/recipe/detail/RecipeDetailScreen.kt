@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomIconButton
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.recipeById.collectAsStateWithLifecycle()

    ScreenContent(
        recipe = state ?: return,
        onEvent = { events ->
            when (events) {
                is RecipeDetailUiEvents.DeleteRecipe -> {
                    viewModel.deleteRecipe(events.remove)
                    navigateBack()
                }
                is RecipeDetailUiEvents.EditRecipe -> { /* TODO: Navigate to edit. */ }
                RecipeDetailUiEvents.NavigateBack -> { navigateBack() }
            }
        }
    )
}

@Composable
private fun ScreenContent(
    recipe: Recipe,
    onEvent: (RecipeDetailUiEvents) -> Unit
) {
    val verticalScroll = rememberScrollState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomCenteredTopBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "NavigateBack",
                        modifier = Modifier
                            .clickable { onEvent(RecipeDetailUiEvents.NavigateBack) }
                    )
                },
                actions = {
                    CustomIconButton(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit recipe.",
                        onClick = {
                            onEvent(RecipeDetailUiEvents.EditRecipe(recipe))
                        }
                    )
                    // TODO: Add alert dialog.
                    CustomIconButton(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Delete recipe.",
                        onClick = {
                            onEvent(RecipeDetailUiEvents.DeleteRecipe(recipe))
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(verticalScroll)
        ) {
            ImageHeader(
                imageUri = recipe.image,
                cameraUri = recipe.cameraImage
            )
            TitleAndInstructionsBody(
                title = recipe.title,
                instructions = recipe.instructions,
                ingredientsAndMeasures = recipe.ingredientsAndMeasures
            )
        }
    }
}

@Composable
private fun ImageHeader(
    imageUri: Uri?,
    cameraUri: Uri?
) {
    val photo = if (imageUri == Uri.EMPTY) cameraUri else imageUri

    Image(
        painter = rememberAsyncImagePainter(photo),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}

/* ----- Body Screen ----- */

@Composable
private fun TitleAndInstructionsBody(
    title: String,
    instructions: String,
    ingredientsAndMeasures: String
) {
    // TODO: Add more data !
    Column(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Instructions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = instructions,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.size(16.dp))
        HorizontalDivider(thickness = 3.dp)
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Ingredients",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = ingredientsAndMeasures,
            textAlign = TextAlign.Start
        )
    }
}

@Preview
@Composable
private fun TitleAndInstructionsBodyPreview() {
    TastyMealTheme {
        TitleAndInstructionsBody(
            title = "Lorem impsum.",
            instructions = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                    "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                    "in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            ingredientsAndMeasures = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
        )
    }
}