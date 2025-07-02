package com.maxidev.tastymeal.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.maxidev.tastymeal.presentation.bookmark.BookmarkScreen
import com.maxidev.tastymeal.presentation.detail.MealDetailScreen
import com.maxidev.tastymeal.presentation.detail.MealDetailScreenOffline
import com.maxidev.tastymeal.presentation.filters.FilterByCategoryScreen
import com.maxidev.tastymeal.presentation.home.HomeScreen
import com.maxidev.tastymeal.presentation.recipe.MyRecipesScreen
import com.maxidev.tastymeal.presentation.recipe.detail.RecipeDetailScreen
import com.maxidev.tastymeal.presentation.recipe.edit.EditRecipeScreen
import com.maxidev.tastymeal.presentation.recipe.new_recipe.NewRecipeScreen
import com.maxidev.tastymeal.presentation.search.SearchScreen
import com.maxidev.tastymeal.presentation.settings.SettingsScreen

fun NavGraphBuilder.homeScreenDestination(navController: NavHostController) {
    composable<NavDestinations.Home> {
        HomeScreen(
            navigateToDetail = { mealId ->
                navController.navigate(NavDestinations.MealDetail(mealId))
            },
            navigateToFilter = { filterId ->
                navController.navigate(NavDestinations.Filters(filterId))
            }
        )
    }
}

fun NavGraphBuilder.searchScreenDestination(navController: NavHostController) {
    composable<NavDestinations.Search> {
        SearchScreen(
            onClick = { mealId ->
                navController.navigate(NavDestinations.MealDetail(mealId))
            }
        )
    }
}

fun NavGraphBuilder.bookmarkScreenDestination(navController: NavHostController) {
    composable<NavDestinations.Bookmarks> {
        BookmarkScreen(
            navigateToDetailOffline = { mealId ->
                navController.navigate(NavDestinations.MealDetailOffline(mealId))
            }
        )
    }
}

fun NavGraphBuilder.settingsScreenDestination() {
    composable<NavDestinations.Settings> {
        SettingsScreen()
    }
}

fun NavGraphBuilder.myRecipesScreenDestination(navController: NavHostController) {
    composable<NavDestinations.MyRecipes> {
        MyRecipesScreen(
            navigateToCreate = { navController.navigate(NavDestinations.NewRecipe) },
            navigateToDetail = { id ->
                navController.navigate(NavDestinations.RecipeDetail(id))
            }
        )
    }
}

fun NavGraphBuilder.newRecipeScreenDestination(navController: NavHostController) {
    composable<NavDestinations.NewRecipe> {
        NewRecipeScreen(
            popBackStack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.mealDetailScreenDestination(navController: NavHostController) {
    composable<NavDestinations.MealDetail> {
        MealDetailScreen(
            navigateBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.mealDetailScreenOfflineDestination(navController: NavHostController) {
    composable<NavDestinations.MealDetailOffline> {
        MealDetailScreenOffline(
            navigateBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.recipeDetailScreenDestination(navController: NavHostController) {
    composable<NavDestinations.RecipeDetail> {
        RecipeDetailScreen(
            navigateBack = { navController.popBackStack() },
            navigateToEdit = { id ->
                navController.navigate(NavDestinations.EditRecipeDetail(id))
            }
        )
    }
}

fun NavGraphBuilder.editRecipeDetailScreenDestination(navController: NavHostController) {
    composable<NavDestinations.EditRecipeDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<NavDestinations.EditRecipeDetail>()

        EditRecipeScreen(
            id = args.recipeId,
            popBackStack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.filtersScreenDestination(navController: NavHostController) {
    composable<NavDestinations.Filters> { mealId ->
        FilterByCategoryScreen(
            navigateBack = { navController.popBackStack() },
            navigateToDetail = {
                navController.navigate(NavDestinations.MealDetail(it))
            }
        )
    }
}