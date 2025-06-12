package com.maxidev.tastymeal.domain.usecase

import com.maxidev.tastymeal.domain.repository.SearchMealRepository
import javax.inject.Inject

class SearchMealUseCase @Inject constructor(
    private val repository: SearchMealRepository
) {
    operator fun invoke(query: String) =
        repository.fetchSearchedMeals(query = query)
}