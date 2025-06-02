package com.maxidev.tastymeal.presentation.recipe.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.domain.usecase.DeleteRecipesUseCase
import com.maxidev.tastymeal.domain.usecase.GetRecipeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeByIdUseCase: GetRecipeByIdUseCase,
    private val deleteRecipeUseCase: DeleteRecipesUseCase
): ViewModel() {

    private val recipeId = checkNotNull(savedStateHandle.get<Long>("recipeId"))

    private val _recipeById = MutableStateFlow<Recipe?>(null)
    val recipeById: StateFlow<Recipe?> = _recipeById.asStateFlow()

    init {
        Log.d("RecipeDetailViewModel", "RecipeId: $recipeId")

        viewModelScope.launch {
            recipeByIdUseCase.invoke(recipeId)
                .collect { data ->
                    _recipeById.value = data
                }
        }
    }

    fun deleteRecipe(recipe: Recipe) =
        viewModelScope.launch {
            deleteRecipeUseCase.invoke(recipe)
        }
}