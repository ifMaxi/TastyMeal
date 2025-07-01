@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.detail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.SmartDisplay
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.maxidev.tastymeal.domain.model.Meal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomButton
import com.maxidev.tastymeal.presentation.components.CustomIconButton
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme
import com.maxidev.tastymeal.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun MealDetailScreen(
    viewModel: MealDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()
    val isBookmark by viewModel.isBookmarked().collectAsStateWithLifecycle(false)

    ScreenContent(
        isBookmarked = isBookmark,
        meal = state,
        onEvent = { event ->
            when (event) {
                MealDetailUiEvents.NavigateBack -> { navigateBack() }
                is MealDetailUiEvents.AddToBookmark -> { viewModel.saveToBookmark(event.add) }
                is MealDetailUiEvents.RemoveToBookmark -> { viewModel.deleteFromBookmark(event.remove) }
            }
        }
    )
}

@Composable
private fun ScreenContent(
    isBookmarked: Boolean,
    meal: Resource<Meal>,
    onEvent: (MealDetailUiEvents) -> Unit
) {
    val context = LocalContext.current
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarState) }) { innerPadding ->
        when (meal) {
            is Resource.Error -> {
                ErrorStateItem(navigateBack = { onEvent(MealDetailUiEvents.NavigateBack) })
            }
            is Resource.Loading -> { LoadingStateItem() }
            is Resource.Success -> {
                val tintColor = if (isBookmarked) Color.Red else LocalContentColor.current

                MealDetailContent(
                    tint = tintColor,
                    navigateBack = { onEvent(MealDetailUiEvents.NavigateBack) },
                    onBookmark = {
                        if (isBookmarked) {
                            onEvent(
                                MealDetailUiEvents.RemoveToBookmark(
                                    remove = meal.data.copy(bookmarked = false)
                                )
                            )
                            scope.launch {
                                snackbarState.showSnackbar(
                                    message = "Removed from bookmarks.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            onEvent(
                                MealDetailUiEvents.AddToBookmark(
                                    add = meal.data.copy(bookmarked = true)
                                )
                            )
                            scope.launch {
                                snackbarState.showSnackbar(
                                    message = "Saved to bookmarks.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    onShare = {
                        val dataStrSource = meal.data.strSource
                        val sendChooser = Intent.createChooser(
                            Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, dataStrSource)
                                type = "text/plain"
                            }, "Share recipe."
                        )

                        if (dataStrSource.isNotEmpty()) {
                            context.startActivity(sendChooser)
                        } else {
                            scope.launch {
                                snackbarState.showSnackbar(
                                    message = "No source available.",
                                    duration = SnackbarDuration.Short,
                                    actionLabel = "Dismiss"
                                )
                            }
                        }
                    },
                    onLink = {
                        val sourceUri = meal.data.strSource.toUri()
                        val browserIntent = Intent(Intent.ACTION_VIEW, sourceUri)

                        if (sourceUri != Uri.EMPTY) {
                            context.startActivity(browserIntent)
                        } else {
                            scope.launch {
                                snackbarState.showSnackbar(
                                    message = "No source available.",
                                    duration = SnackbarDuration.Short,
                                    actionLabel = "Dismiss"
                                )
                            }
                        }
                    },
                    onWatchInYoutube = {
                        val youTubeUri = meal.data.strYouTube.toUri()
                        val youTubeIntent = Intent(Intent.ACTION_VIEW, youTubeUri)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                        if (youTubeUri != Uri.EMPTY) {
                            context.startActivity(youTubeIntent)
                        } else {
                            scope.launch {
                                snackbarState.showSnackbar(
                                    message = "No video available.",
                                    duration = SnackbarDuration.Short,
                                    actionLabel = "Dismiss"
                                )
                            }
                        }
                    },
                    meal = meal.data ?: return@Scaffold,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

/* ----- Content ----- */

@Composable
fun MealDetailContent(
    modifier: Modifier = Modifier,
    meal: Meal,
    tint: Color,
    onShare: () -> Unit,
    onBookmark: () -> Unit,
    onLink: () -> Unit,
    onWatchInYoutube: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
        val tabList = listOf("Instructions", "Ingredients", "Resources")

        ImageWithTextHeader(
            strMeal = meal.strMeal,
            strMealThumb = meal.strMealThumb,
            strCategory = meal.strCategory,
            tint = tint,
            onBookmark = onBookmark,
            navigateBack = navigateBack
        )
        PrimaryTabRow(
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
                InstructionsBody(strInstructions = meal.strInstructions)
            }
            1 -> {
                IngredientsBody(
                    ingredients = meal.strIngredients,
                    measureIng = meal.strMeasure
                )
            }
            2 -> {
                ButtonActionsItem(
                    onShare = onShare,
                    onLink = onLink,
                    onWatchInYoutube = onWatchInYoutube
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

/* ----- Header ----- */

@Composable
private fun ImageWithTextHeader(
    strMeal: String,
    strMealThumb: String,
    strCategory: String,
    tint: Color,
    onBookmark: () -> Unit,
    navigateBack: () -> Unit
) {
    Box {
        CustomAsyncImage(
            model = strMealThumb,
            contentDescription = strMeal,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
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
                text = strMeal,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = strCategory,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 16.dp, end = 16.dp, bottom = 6.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIconButton(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = "Navigate back.",
                onClick = navigateBack
            )
            CustomIconButton(
                imageVector = Icons.Rounded.Bookmark,
                contentDescription = "Add to bookmarks.",
                tint = tint,
                onClick = onBookmark
            )
        }
    }
}

@Preview
@Composable
private fun ImageWithTextHeaderPreview() {
    TastyMealTheme {
        ImageWithTextHeader(
            strMeal = "Lorem Impsum",
            strMealThumb = "Image",
            strCategory = "Category",
            onBookmark = {},
            navigateBack = {},
            tint = Color.Red
        )
    }
}

/* ----- Button actions ----- */

@Composable
private fun ButtonActionsItem(
    onShare: () -> Unit,
    onLink: () -> Unit,
    onWatchInYoutube: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(
            imageVector = Icons.Rounded.Link,
            contentDescription = "View in browser.",
            onClick = onLink,
            name = "View in browser",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        CustomButton(
            imageVector = Icons.Rounded.SmartDisplay,
            contentDescription = "Watch on youtube.",
            onClick = onWatchInYoutube,
            name = "Watch on youtube",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        CustomButton(
            imageVector = Icons.Rounded.Share,
            contentDescription = "Share recipe.",
            onClick = onShare,
            name = "Share recipe",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
    }
}

@Preview
@Composable
private fun ButtonActionsItemPreview() {
    ButtonActionsItem(
        onShare = {},
        onLink = {},
        onWatchInYoutube = {}
    )
}

/* ----- Instructions body ----- */

@Composable
private fun InstructionsBody(
    strInstructions: String
) {
    val regex = "(?<=\\.)\\s*(\\r\\n|\\n)".toRegex()
    val steps = regex.split(strInstructions.trim()).map { it.trim() }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Instructions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(20.dp))

        steps.forEachIndexed { index, step ->
            OutlinedCard(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.outlinedCardElevation(6.dp)
            ) {
                Text(
                    text = "Step ${index + 1}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 6.dp)
                )
                Text(
                    text = step,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 16.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Preview
@Composable
private fun InstructionsBodyPreview() {
    TastyMealTheme {
        InstructionsBody(
            strInstructions = "Preheat oven to 350° F. Spray a 9x13-inch baking pan with " +
                    "non-stick spray.\r\nCombine soy sauce, ½ cup water, brown sugar, ginger and " +
                    "garlic in a small saucepan and cover. Bring to a boil over medium heat. " +
                    "Remove lid and cook for one minute once boiling.\r\nMeanwhile, stir together " +
                    "the corn starch and 2 tablespoons of water in a separate dish until smooth. " +
                    "Once sauce is boiling, add mixture to the saucepan and stir to combine. " +
                    "Cook until the sauce starts to thicken then remove from heat.\r\nPlace the " +
                    "chicken breasts in the prepared pan. Pour one cup of the sauce over top of " +
                    "chicken. Place chicken in oven and bake 35 minutes or until cooked through. " +
                    "Remove from oven and shred chicken in the dish using two forks.\r\n*Meanwhile, " +
                    "steam or cook the vegetables according to package directions.\r\nAdd the " +
                    "cooked vegetables and rice to the casserole dish with the chicken. " +
                    "Add most of the remaining sauce, reserving a bit to drizzle over the top when " +
                    "serving. Gently toss everything together in the casserole dish until combined. " +
                    "Return to oven and cook 15 minutes. Remove from oven and let " +
                    "stand 5 minutes before serving. Drizzle each serving with remaining sauce. Enjoy!"
        )
    }
}

/* ----- Ingredients body ----- */

@Composable
private fun IngredientsBody(
    ingredients: List<String>,
    measureIng: List<String>
) {
    // Join the lists together to create a list of pairs.
    val zipLists = ingredients.zip(measureIng)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Ingredients",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(20.dp))

        OutlinedCard(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.outlinedCardElevation(6.dp)
        ) {
            zipLists.forEach { (ingr, quantity) ->
                ListItem(
                    headlineContent = { Text(text = ingr) },
                    trailingContent = {
                        Text(
                            text = quantity,
                            fontSize = 14.sp
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
private fun IngredientsBodyPreview() {
    TastyMealTheme {
        IngredientsBody(
            ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3"),
            measureIng = listOf("Measure 1", "Measure 2", "Measure 3")
        )
    }
}

/* ----- Loading and error states ----- */

@Composable
fun LoadingStateItem() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .progressSemantics()
        )
    }
}

@Composable
private fun ErrorStateItem(navigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CustomIconButton(
            imageVector = Icons.Rounded.ArrowBackIosNew,
            contentDescription = "Navigate back.",
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )
        Text(
            text = "Something is wrong... Please try again or check your internet connection.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Preview
@Composable
private fun LoadingStateItemPreview() {
    LoadingStateItem()
    ErrorStateItem(navigateBack = {})
}