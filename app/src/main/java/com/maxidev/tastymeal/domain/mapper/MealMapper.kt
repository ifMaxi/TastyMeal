package com.maxidev.tastymeal.domain.mapper

import com.maxidev.tastymeal.data.remote.dto.MealListDto
import com.maxidev.tastymeal.domain.model.Meal

fun MealListDto.asDomain() =
    this.meals.map { data ->
        Meal(
            idMeal = data.idMeal.orEmpty(),
            strMeal = data.strMeal.orEmpty(),
            strMealThumb = data.strMealThumb.orEmpty(),
            strCategory = data.strCategory.orEmpty(),
            strInstructions = data.strInstructions.orEmpty(),
            strTags = data.strTags.orEmpty(),
            strYouTube = data.strYoutube.orEmpty(),
            strSource = data.strSource.orEmpty(),
            strIngredients = listOfNotNull(
                data.strIngredient1,
                data.strIngredient2,
                data.strIngredient3,
                data.strIngredient4,
                data.strIngredient5,
                data.strIngredient6,
                data.strIngredient7,
                data.strIngredient8,
                data.strIngredient9,
                data.strIngredient10,
                data.strIngredient11,
                data.strIngredient12,
                data.strIngredient13,
                data.strIngredient14,
                data.strIngredient15,
                data.strIngredient16,
                data.strIngredient17,
                data.strIngredient18,
                data.strIngredient19,
                data.strIngredient20
            ).filter { it.isNotBlank() },
            strMeasure = listOfNotNull(
                data.strMeasure1,
                data.strMeasure2,
                data.strMeasure3,
                data.strMeasure4,
                data.strMeasure5,
                data.strMeasure6,
                data.strMeasure7,
                data.strMeasure8,
                data.strMeasure9,
                data.strMeasure10,
                data.strMeasure11,
                data.strMeasure12,
                data.strMeasure13,
                data.strMeasure14,
                data.strMeasure15,
                data.strMeasure16,
                data.strMeasure17,
                data.strMeasure18,
                data.strMeasure19,
                data.strMeasure20
            ).filter { it.isNotBlank() }
        )
    }