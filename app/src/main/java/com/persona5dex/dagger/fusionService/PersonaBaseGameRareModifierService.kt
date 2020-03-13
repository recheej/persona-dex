package com.persona5dex.dagger.fusionService

import android.content.Context
import com.persona5dex.models.Enumerations

abstract class PersonaRareFileModifierService(context: Context) : PersonaRareModifierService, PersonaFileService<RarePersonaModificationManager>(context) {
    override suspend fun getRareModifierManager(): RarePersonaModificationManager = parseFile()
}