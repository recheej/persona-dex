package com.persona5dex.dagger.fusionService

import com.persona5dex.models.Enumerations

data class FusionChart(private val fusionMap: Map<Enumerations.Arcana, Map<Enumerations.Arcana, Enumerations.Arcana>>) {
    fun getResultArcana(arcana: Enumerations.Arcana, arcanaTwo: Enumerations.Arcana) =
            fusionMap[arcana]?.get(arcanaTwo)
}