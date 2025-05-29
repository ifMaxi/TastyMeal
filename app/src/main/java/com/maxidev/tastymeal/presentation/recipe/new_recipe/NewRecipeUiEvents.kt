package com.maxidev.tastymeal.presentation.recipe.new_recipe

import com.maxidev.tastymeal.domain.model.Recipe

sealed interface NewRecipeUiEvents {
    data class AddToDataBase(val add: Recipe): NewRecipeUiEvents
}