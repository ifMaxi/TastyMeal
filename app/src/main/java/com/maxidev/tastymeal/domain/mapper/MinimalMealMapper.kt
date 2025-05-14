package com.maxidev.tastymeal.domain.mapper

import com.maxidev.tastymeal.data.local.entity.MinimalMealEntity
import com.maxidev.tastymeal.data.remote.dto.MealMinimalListDto
import com.maxidev.tastymeal.domain.model.MinimalMeal

fun MealMinimalListDto.asEntity() =
    this.meals.map { data ->
        MinimalMealEntity(
            idMeal = data.idMeal.orEmpty(),
            strMeal = data.strMeal.orEmpty(),
            strMealThumb = data.strMealThumb.orEmpty(),
            strCategory = data.strCategory.orEmpty()
        )
    }

fun MealMinimalListDto.asDomain() =
    this.meals.map { data ->
        MinimalMeal(
            idMeal = data.idMeal.orEmpty(),
            strMeal = data.strMeal.orEmpty(),
            strMealThumb = data.strMealThumb.orEmpty(),
            strCategory = data.strCategory.orEmpty()
        )
    }

fun MinimalMealEntity.asDomain() =
    MinimalMeal(
        idMeal = idMeal,
        strMeal = strMeal,
        strMealThumb = strMealThumb,
        strCategory = strCategory
    )