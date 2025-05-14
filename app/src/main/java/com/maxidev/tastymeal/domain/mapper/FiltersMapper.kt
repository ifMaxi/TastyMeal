package com.maxidev.tastymeal.domain.mapper

import com.maxidev.tastymeal.data.local.entity.FiltersEntity
import com.maxidev.tastymeal.data.remote.dto.FilterDto
import com.maxidev.tastymeal.domain.model.FilterByCountryMeal

fun FilterDto.asEntity() =
    this.meals?.map { data ->
        FiltersEntity(
            idMeal = data?.idMeal.orEmpty(),
            strMeal = data?.strMeal.orEmpty(),
            strMealThumb = data?.strMealThumb.orEmpty()
        )
    }

fun FiltersEntity.asDomain() =
    FilterByCountryMeal(
        idMeal = idMeal,
        strMeal = strMeal,
        strMealThumb = strMealThumb
    )