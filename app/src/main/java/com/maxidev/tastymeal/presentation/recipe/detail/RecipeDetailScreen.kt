@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.IncompleteCircle
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomIconButton

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToEdit: (Long) -> Unit
) {
    val state by viewModel.recipeById.collectAsStateWithLifecycle()

    ScreenContent(
        recipe = state ?: return,
        navigateToEdit = navigateToEdit,
        onEvent = { events ->
            when (events) {
                is RecipeDetailUiEvents.DeleteRecipe -> {
                    viewModel.deleteRecipe(events.remove)
                    navigateBack()
                }
                RecipeDetailUiEvents.NavigateBack -> { navigateBack() }
            }
        }
    )
}

@Composable
private fun ScreenContent(
    recipe: Recipe,
    navigateToEdit: (Long) -> Unit,
    onEvent: (RecipeDetailUiEvents) -> Unit
) {
    val verticalScroll = rememberScrollState()
    var openDialog by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(verticalScroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImageHeader(
                title = recipe.title,
                imageUri = recipe.image,
                cameraUri = recipe.cameraImage,
                navigateBack = { onEvent(RecipeDetailUiEvents.NavigateBack) },
                onEdit = { navigateToEdit(recipe.id) },
                openDialog = { openDialog = true }
            )

            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
            val tabList = listOf("Overview", "Instructions", "Ingredients", "Notes")

            PrimaryScrollableTabRow(
                selectedTabIndex = selectedIndex,
                tabs = {
                    tabList.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            text = { Text(text = tab) }
                        )
                    }
                }
            )

            when (selectedIndex) {
                0 -> {
                    OverviewTabItem(
                        portions = recipe.portions,
                        preparationTime = recipe.preparationTime,
                        cookingTime = recipe.cookingTime,
                        source = recipe.source
                    )
                }
                1 -> { StepsTabItem(steps = recipe.instructions) }
                2 -> { IngredientsTabItem(ingredients = recipe.ingredientsAndMeasures) }
                3 -> { NotesTabItem(notes = recipe.notes) }
            }
            Spacer(modifier = Modifier.size(20.dp))
        }

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(RecipeDetailUiEvents.DeleteRecipe(recipe))
                            openDialog = false
                        },
                        content = { Text(text = "Confirm") }
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = { openDialog = false },
                        content = { Text(text = "Dismiss") }
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.WarningAmber,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = "Wait!"
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete your recipe?",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
private fun ImageHeader(
    title: String,
    imageUri: Uri?,
    cameraUri: Uri?,
    navigateBack: () -> Unit,
    onEdit: () -> Unit,
    openDialog: () -> Unit
) {
    Box {
        val photo = if (imageUri == Uri.EMPTY) cameraUri else imageUri

        if (photo != Uri.EMPTY) {
            Image(
                painter = rememberAsyncImagePainter(photo),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
        OutlinedCard(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.outlinedCardElevation(6.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIconButton(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = "Navigate back.",
                onClick = navigateBack
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomIconButton(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Edit recipe.",
                onClick = onEdit
            )
            CustomIconButton(
                imageVector = Icons.Rounded.DeleteOutline,
                contentDescription = "Delete recipe.",
                onClick = openDialog
            )
        }
    }
}

/* ----- Tabs items -----*/

@Composable
private fun OverviewTabItem(
    portions: String,
    preparationTime: String,
    cookingTime: String,
    source: String
) {
    val context = LocalContext.current
    val browserIntent = Intent(Intent.ACTION_VIEW, source.toUri())

    OutlinedCard(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.outlinedCardElevation(6.dp)
    ) {
        val textProvider = "Not provided."
        val isPortionsEmpty = portions.ifEmpty { textProvider }
        val isCookingTimeEmpty = cookingTime.ifEmpty { textProvider }
        val isPreparationTimeEmpty = preparationTime.ifEmpty { textProvider }
        val isSourceEmpty = source.ifEmpty { textProvider }

        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.IncompleteCircle,
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = "Portions: $isPortionsEmpty") }
        )
        HorizontalDivider()
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.Timer,
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = "Preparation time: $isPreparationTimeEmpty") }
        )
        HorizontalDivider()
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.Timelapse,
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = "Cooking time: $isCookingTimeEmpty") }
        )
        HorizontalDivider()
        ListItem(
            modifier = Modifier
                .clickable {
                    if (source.isNotEmpty()) {
                        context.startActivity(browserIntent)
                    }
                },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.Link,
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = "Source: $isSourceEmpty") }
        )
    }
}

@Preview
@Composable
private fun OverviewTabItemPreview() {
    OverviewTabItem(
        portions = "8",
        preparationTime = "54 min.",
        cookingTime = "140 min.",
        source = "Link."
    )
}

@Composable
private fun StepsTabItem(
    steps: String
) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.outlinedCardElevation(6.dp)
    ) {
        val ifStepsIsEmpty = steps.ifEmpty { "Nothing to show here." }

        Text(
            text = ifStepsIsEmpty,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun StepsTabItemPreview() {
    StepsTabItem(
        steps = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                "in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
    )
}

@Composable
private fun IngredientsTabItem(
    ingredients: String
) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.outlinedCardElevation(6.dp)
    ) {
        val ifIngredientsIsEmpty = ingredients.ifEmpty { "Nothing to show here." }

        Text(
            text = ifIngredientsIsEmpty,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun IngredientsTabItemPreview() {
    IngredientsTabItem(
        ingredients = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )
}

@Composable
private fun NotesTabItem(
    notes: String
) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.outlinedCardElevation(6.dp)
    ) {
        val ifNotesIsEmpty = notes.ifEmpty { "Nothing to show here." }

        Text(
            text = ifNotesIsEmpty,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}