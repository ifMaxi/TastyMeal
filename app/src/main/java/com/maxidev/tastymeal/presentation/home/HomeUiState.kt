package com.maxidev.tastymeal.presentation.home

import androidx.paging.PagingData
import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.FilterByCountryMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUiState(
    //val randomMeal: List<MinimalMeal> = emptyList(),
    val categories: List<CategoriesMeal> = emptyList(),
    val filterByCountry: List<FilterByCountryMeal> = emptyList(),
    val searchByLetter: Flow<PagingData<MinimalMeal>> = emptyFlow()
)