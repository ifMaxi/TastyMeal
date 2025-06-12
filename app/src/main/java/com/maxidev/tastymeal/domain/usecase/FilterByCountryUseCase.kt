package com.maxidev.tastymeal.domain.usecase

import com.maxidev.tastymeal.domain.repository.FiltersRepository
import javax.inject.Inject

class GetFiltersByCategoryUseCase @Inject constructor(
    private val repository: FiltersRepository
) {
    suspend operator fun invoke(category: String) =
        repository.fetchFilterByCategory(category)
}