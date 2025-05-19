package com.maxidev.tastymeal.domain.usecase

import com.maxidev.tastymeal.domain.repository.MealDetailRepository
import javax.inject.Inject

class MealDetailUseCase @Inject constructor(
    private val repository: MealDetailRepository
) {

    suspend operator fun invoke(id: String) =
        repository.fetchMealById(id)
}