package com.maxidev.tastymeal.presentation.recipe.edit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.domain.usecase.GetRecipeByIdUseCase
import com.maxidev.tastymeal.domain.usecase.UpsertRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeDetailViewModel @Inject constructor(
    private val upsertRecipeUseCase: UpsertRecipeUseCase,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase
): ViewModel() {

    val titleTextState = TextFieldState()
    val instructionsTextState = TextFieldState()
    val ingredientsAndMeasuresState = TextFieldState()

    fun getId(id: Long) = viewModelScope.launch {
        getRecipeByIdUseCase.invoke(id)
            .collect {
                titleTextState.setTextAndPlaceCursorAtEnd(text = it?.title.toString())
                instructionsTextState.setTextAndPlaceCursorAtEnd(text = it?.instructions.toString())
                ingredientsAndMeasuresState.setTextAndPlaceCursorAtEnd(
                    text = it?.ingredientsAndMeasures.toString()
                )
            }
    }

    fun editRecipe(recipe: Recipe) =
        viewModelScope.launch {
            upsertRecipeUseCase.invoke(recipe)
        }
}