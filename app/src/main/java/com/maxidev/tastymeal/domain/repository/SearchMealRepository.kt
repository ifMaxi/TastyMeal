package com.maxidev.tastymeal.domain.repository

import androidx.paging.PagingData
import com.maxidev.tastymeal.domain.model.MinimalMeal
import kotlinx.coroutines.flow.Flow

interface SearchMealRepository {

    fun fetchSearchedMeals(
        query: String,
        //queryByLetter: String?
    ): Flow<PagingData<MinimalMeal>>
}