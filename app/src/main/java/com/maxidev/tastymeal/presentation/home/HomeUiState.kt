package com.maxidev.tastymeal.presentation.home

import androidx.paging.PagingData
import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUiState(
    val categories: List<CategoriesMeal> = emptyList(),
    val searchByLetter: Flow<PagingData<MinimalMeal>> = emptyFlow()
)