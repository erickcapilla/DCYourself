package com.erickcapilla.dcyourself.provider.services.firebase

import com.erickcapilla.dcyourself.model.DataQuestions

class QuestionProvider {
    companion object {
        val questionsList = listOf(
            DataQuestions("¿Pregunta 1?"),
            DataQuestions("¿Pregunta 2?"),
            DataQuestions("¿Pregunta 3?"),
            DataQuestions("¿Pregunta 4?"),
            DataQuestions("¿Pregunta 5?"),
            DataQuestions("¿Pregunta 6?")
        )
    }
}