package com.persona5dex.dagger.fusionService

import com.persona5dex.models.Enumerations

interface FusionChartService {
    suspend fun getFusionChart(): FusionChart
}