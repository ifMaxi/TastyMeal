package com.maxidev.tastymeal.navigation

import kotlinx.serialization.Serializable

sealed interface NavDestinations {
    @Serializable data object Home : NavDestinations
    @Serializable data object Search : NavDestinations
    @Serializable data object Bookmarks: NavDestinations
    @Serializable data object Settings : NavDestinations
    @Serializable data object MyRecipes: NavDestinations
    @Serializable data object NewRecipe: NavDestinations
    @Serializable data class MealDetail(val mealId: String) : NavDestinations
    @Serializable data class MealDetailOffline(val mealId: String): NavDestinations
    @Serializable data class RecipeDetail(val recipeId: Long): NavDestinations
    @Serializable data class EditRecipeDetail(val recipeId: Long): NavDestinations
    @Serializable data class Filters(val filterId: String): NavDestinations
}