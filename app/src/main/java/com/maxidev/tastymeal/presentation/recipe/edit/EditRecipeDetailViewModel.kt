package com.maxidev.tastymeal.presentation.recipe.edit

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.domain.usecase.GetRecipeByIdUseCase
import com.maxidev.tastymeal.domain.usecase.UpsertRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeDetailViewModel @Inject constructor(
    private val upsertRecipeUseCase: UpsertRecipeUseCase,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase
): ViewModel() {

    // State for text fields.
    val titleTextState = TextFieldState()
    val portionsTextState = TextFieldState()
    val preparationTimeTextState = TextFieldState()
    val cookingTimeTextState = TextFieldState()
    val sourceTextState = TextFieldState()
    val instructionsTextState = TextFieldState()
    val ingredientsAndMeasuresState = TextFieldState()
    val notesTextState = TextFieldState()

    // State for gallery image and camera.
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    private val _cameraImageUri = MutableStateFlow<Uri?>(null)
    val cameraImageUri = _cameraImageUri.asStateFlow()

    // Groups states and initializes them when navigating using a Launched effect composable.
    // This will display the data if it exists in the database.
    fun getId(id: Long) = viewModelScope.launch {
        getRecipeByIdUseCase.invoke(id)
            .collect {
                titleTextState.setTextAndPlaceCursorAtEnd(it?.title.toString())
                instructionsTextState.setTextAndPlaceCursorAtEnd(it?.instructions.toString())
                ingredientsAndMeasuresState.setTextAndPlaceCursorAtEnd(it?.ingredientsAndMeasures.toString())
                sourceTextState.setTextAndPlaceCursorAtEnd(it?.source.toString())
                preparationTimeTextState.setTextAndPlaceCursorAtEnd(it?.preparationTime.toString())
                cookingTimeTextState.setTextAndPlaceCursorAtEnd(it?.cookingTime.toString())
                portionsTextState.setTextAndPlaceCursorAtEnd(it?.portions.toString())
                notesTextState.setTextAndPlaceCursorAtEnd(it?.notes.toString())
                _imageUri.value = if (it?.image != Uri.EMPTY) it?.image else null
                _cameraImageUri.value = if (it?.cameraImage != Uri.EMPTY) it?.cameraImage else null
            }
    }

    // Callback from image states.
    fun updateImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun updateCameraImageUri(uri: Uri?) {
        _cameraImageUri.value = uri
    }

    // Updates the recipe if it exists in the database with the same identifier.
    fun editRecipe(recipe: Recipe) =
        viewModelScope.launch {
            upsertRecipeUseCase.invoke(recipe)
        }
}