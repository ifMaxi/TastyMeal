package com.maxidev.tastymeal.domain.usecase

import com.maxidev.tastymeal.domain.repository.HomeRepository
import javax.inject.Inject

class RandomMealUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() =
        repository.fetchRandomMeal()
}

class CategoriesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke() =
        repository.fetchCategories()
}