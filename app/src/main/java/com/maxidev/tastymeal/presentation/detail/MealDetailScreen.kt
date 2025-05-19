@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Source
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.tastymeal.domain.model.Meal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomExtendedFab
import com.maxidev.tastymeal.presentation.components.CustomIconButton
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme
import com.maxidev.tastymeal.utils.Resource

// TODO: Favorite functionality.

@Composable
fun MealDetailScreen(
    viewModel: MealDetailViewModel = hiltViewModel()
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()

    ScreenContent(meal = state)
}

@Composable
private fun ScreenContent(
    meal: Resource<Meal>
) {
    Scaffold(
        topBar = {
            CustomCenteredTopBar(
                title = { /* Do nothing. */ },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable { /* TODO: Navigate back */ }
                    )
                },
                actions = {
                    CustomIconButton(
                        imageVector = Icons.Rounded.BookmarkBorder,
                        contentDescription = "Bookmark",
                        onClick = { /* TODO: Bookmark logic. */ }
                    )
                    CustomIconButton(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                        onClick = { /* TODO: Share intent. */ }
                    )
                    // TODO: If source not exist. Hide this button.
                    CustomIconButton(
                        imageVector = Icons.Rounded.Source,
                        contentDescription = "Source",
                        onClick = { /* TODO: Browser intent. */ }
                    )
                }
            )
        },
        floatingActionButton = {
            // TODO: If not exist youtube link, hide this button or show a message.
            CustomExtendedFab(
                onClick = { /* TODO: Open on youtube intent. */ },
                text = { Text(text = "Watch on YouTube") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PlayCircle,
                        contentDescription = "Watch on YouTube"
                    )
                },
                shape = RoundedCornerShape(10.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        when (meal) {
            is Resource.Error -> { Text(text = meal.message.orEmpty()) }
            is Resource.Loading -> {
                CircularProgressIndicator()
            }
            is Resource.Success -> {
                MealDetailContent(
                    meal = meal.data ?: return@Scaffold,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun MealDetailContent(
    modifier: Modifier = Modifier,
    meal: Meal
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageWithTextHeader(
            strMeal = meal.strMeal,
            strMealThumb = meal.strMealThumb,
            strCategory = meal.strCategory
        )
        InstructionsBody(strInstructions = meal.strInstructions)
        IngredientsBody(
            ingredients = meal.strIngredients,
            measureIng = meal.strMeasure
        )
        TagsBottom(tags = meal.strTags)
    }
}

/* ----- Header ----- */

@Composable
private fun ImageWithTextHeader(
    strMeal: String,
    strMealThumb: String,
    strCategory: String
) {
    Box {
        CustomAsyncImage(
            model = strMealThumb,
            contentDescription = strMeal,
            contentScale = ContentScale.Crop,
            height = 300.dp,
            onClick = { /* TODO: Zoom image. */ },
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    val colors = listOf(Color.Black, Color.Black, Color.Transparent)

                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors = colors),
                        blendMode = BlendMode.DstIn
                    )
                }
        )
        Column(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .align(Alignment.BottomStart),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = strMeal,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            Text(
                text = strCategory
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
            strCategory = "Category"
        )
    }
}

/* ----- Instructions body ----- */

@Composable
private fun InstructionsBody(
    strInstructions: String
) {
    val regex = "(?<=\\.)\\s*(\\r\\n|\\n)".toRegex()
    val steps = regex.split(strInstructions.trim()).map { it.trim() }

    Column {
        Text(text = "Instructions")
        steps.forEachIndexed { index, step ->
            Text(
                text = "Step ${index + 1}: $step",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            )
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
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
    ) {
        Text(text = "Ingredients")

        zipLists.forEach { (ingr, quantity) ->
            ListItem(
                headlineContent = { Text(text = ingr) },
                trailingContent = { Text(text = quantity) }
            )
            HorizontalDivider()
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

/* ----- Tags bottom ----- */

@Composable
private fun TagsBottom(
    tags: String
) {
    val tagsList = tags.split(",")
    val maxItems = 4

    // TODO: If not exist tags, hide this section.
    Column {
        Text(text = "Tags")
        FlowRow(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top)
                .fillMaxWidth(),
            maxItemsInEachRow = maxItems,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tagsList.forEach { tag ->
                Box(
                    Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(4.dp)
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagsBottomPreview() {
    TastyMealTheme {
        TagsBottom(tags = "Meat,Casserole,Dinner,Main Course,Vegetable,Quick & Easy")
    }
}