package com.maxidev.tastymeal.presentation.search

import androidx.paging.PagingData
import com.maxidev.tastymeal.domain.model.MinimalMeal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val search: Flow<PagingData<MinimalMeal>> = emptyFlow()
)