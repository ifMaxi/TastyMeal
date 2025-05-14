package com.maxidev.tastymeal.domain.usecase

import com.maxidev.tastymeal.domain.repository.HomeRepository
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    operator fun invoke() =
        repository.fetchCategories()
}