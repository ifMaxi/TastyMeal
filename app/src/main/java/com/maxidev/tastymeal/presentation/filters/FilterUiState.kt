package com.maxidev.tastymeal.presentation.filters

import com.maxidev.tastymeal.domain.model.FilterByCategory

data class FilterUiState(
    val categories: List<FilterByCategory> = emptyList()
)