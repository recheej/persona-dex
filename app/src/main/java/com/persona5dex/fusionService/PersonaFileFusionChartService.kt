package com.persona5dex.fusionService

import android.content.Context

abstract class PersonaFileFusionChartService(
        context: Context
) : FusionChartService, PersonaJsonFileService<FusionChart>(context) {

    final override suspend fun getFusionChart(): FusionChart = parseFile()
}