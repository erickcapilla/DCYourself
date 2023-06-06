package com.erickcapilla.dcyourself.provider.services.firebase

import com.erickcapilla.dcyourself.model.DataRecommendations

class RecommendationProvider {
    companion object {
        val recommendationNutritionalList = listOf(
            DataRecommendations("Controla los carbohidratos", "Opta por carbohidratos complejos y de bajo índice glucémico, como granos enteros, legumbres, frutas y verduras. Estos alimentos liberan glucosa de manera más lenta en el torrente sanguíneo, evitando picos de azúcar en la sangre."),
            DataRecommendations("Aumenta el consumo de fibra", "Los alimentos ricos en fibra, como frutas, verduras, legumbres y granos enteros, pueden ayudar a controlar los niveles de azúcar en la sangre y mejorar la sensibilidad a la insulina. Además, la fibra promueve la saciedad y ayuda a mantener un peso saludable."),
            DataRecommendations("Elige grasas saludables", "Incorpora fuentes de grasas saludables en tu dieta, como aguacate, aceite de oliva, nueces, semillas y pescados grasos como el salmón. Estas grasas son beneficiosas para la salud cardiovascular y pueden ayudar a prevenir la resistencia a la insulina."),
            DataRecommendations("Limita el consumo de azúcares añadidos", "Evita o limita el consumo de alimentos y bebidas azucaradas, como refrescos, jugos procesados, dulces y productos de repostería. El exceso de azúcar puede contribuir al aumento de peso y al desarrollo de resistencia a la insulina."),
            DataRecommendations("Controla las porciones", "Presta atención al tamaño de las porciones para evitar el exceso de calorías y carbohidratos. Utiliza platos más pequeños y presta atención a las etiquetas de los alimentos para tener una idea clara de las porciones recomendadas.")
        )

        val recommendationExerciseList = listOf(
            DataRecommendations("Caminar", "Caminar es una actividad física accesible y de bajo impacto que puedes realizar en cualquier momento y lugar. Intenta caminar al menos 30 minutos al día, preferiblemente después de las comidas, ya que esto puede ayudar a controlar los niveles de azúcar en la sangre."),
            DataRecommendations("Correr o trotar", "Si tienes una condición física adecuada, correr o trotar puede ser una excelente opción para quemar calorías y fortalecer el sistema cardiovascular. Comienza con sesiones cortas y gradualmente aumenta la duración y la intensidad."),
            DataRecommendations("Montar en bicicleta", "Montar en bicicleta es una actividad física de bajo impacto que puede ser disfrutada en exteriores o en una bicicleta estática en interiores. Puedes realizar paseos al aire libre o participar en clases de spinning."),
            DataRecommendations("Nadar", "La natación es una actividad física de bajo impacto que ejercita todo el cuerpo y no pone estrés en las articulaciones. Además, la resistencia del agua proporciona un buen ejercicio cardiovascular. Si no sabes nadar, puedes probar con aeróbicos acuáticos o aqua fitness."),
            DataRecommendations("Entrenamiento de fuerza", "El entrenamiento de fuerza, ya sea con pesas, máquinas o el uso de tu propio peso corporal, es fundamental para desarrollar masa muscular y aumentar el metabolismo. Realiza ejercicios que trabajen diferentes grupos musculares al menos dos veces por semana.")
        )
    }
}