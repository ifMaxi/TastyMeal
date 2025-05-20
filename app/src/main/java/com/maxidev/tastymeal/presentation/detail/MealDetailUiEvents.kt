package com.maxidev.tastymeal.presentation.detail

import com.maxidev.tastymeal.domain.model.Meal

sealed interface MealDetailUiEvents {
    data object NavigateBack : MealDetailUiEvents
    data class AddToBookmark(val add: Meal): MealDetailUiEvents
    data class RemoveToBookmark(val remove: Meal) : MealDetailUiEvents
}