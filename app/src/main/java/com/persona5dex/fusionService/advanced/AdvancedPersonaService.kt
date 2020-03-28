package com.persona5dex.fusionService.advanced

import android.content.Context
import com.persona5dex.fusionService.PersonaJsonFileService

interface AdvancedPersonaService {
    suspend fun getAdvancedPersonas(): List<AdvancedPersonaFusion>
}

abstract class AdvancedPersonaJsonFileService(context: Context) :
        AdvancedPersonaService, PersonaJsonFileService<List<AdvancedPersonaFusion>>(context) {
    override suspend fun getAdvancedPersonas(): List<AdvancedPersonaFusion> = parseFile()
}
