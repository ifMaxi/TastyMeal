package com.maxidev.tastymeal.presentation.bookmark

import com.maxidev.tastymeal.domain.model.Meal

data class BookmarkUiState(
    val allBookmarks: List<Meal> = emptyList()
)