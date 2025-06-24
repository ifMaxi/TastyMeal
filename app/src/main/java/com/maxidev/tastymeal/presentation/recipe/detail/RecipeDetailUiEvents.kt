package com.maxidev.tastymeal.presentation.recipe.detail

import com.maxidev.tastymeal.domain.model.Recipe

sealed interface RecipeDetailUiEvents {
    data object NavigateBack: RecipeDetailUiEvents
    data class DeleteRecipe(val remove: Recipe): RecipeDetailUiEvents
}