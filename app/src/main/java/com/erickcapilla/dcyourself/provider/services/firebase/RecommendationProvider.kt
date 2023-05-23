package com.erickcapilla.dcyourself.provider.services.firebase

import com.erickcapilla.dcyourself.model.DataRecommendations

class RecommendationProvider {
    companion object {
        val recommendationNutritionalList = listOf(
            DataRecommendations("Recomendación nut 1", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación nut 2", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación nut 3", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación nut 4", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación nut 5", "Descripción de la recomendación para el usuario")
        )

        val recommendationExerciseList = listOf(
            DataRecommendations("Recomendación ex 1", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación ex 2", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación ex 3", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación ex 4", "Descripción de la recomendación para el usuario"),
            DataRecommendations("Recomendación ex 5", "Descripción de la recomendación para el usuario")
        )
    }
}