package com.maxidev.tastymeal.presentation.recipe.new_recipe

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.domain.usecase.UpsertRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val upsertRecipeUseCase: UpsertRecipeUseCase
): ViewModel() {

    val titleTextState = TextFieldState()
    val instructionsTextState = TextFieldState()
    val ingredientsAndMeasuresState = TextFieldState()

    fun saveRecipe(recipe: Recipe) =
        viewModelScope.launch {
            upsertRecipeUseCase.invoke(recipe)
        }
}