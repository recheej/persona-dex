package com.persona5dex.fusionService

import com.persona5dex.fusionService.FusionChart

interface FusionChartService {
    suspend fun getFusionChart(): FusionChart
}