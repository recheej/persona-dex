package com.persona5dex.fusionService

import android.content.Context

abstract class PersonaRareFileModifierService(context: Context) : PersonaRareModifierService, PersonaJsonFileService<RarePersonaModificationManager>(context) {
    override suspend fun getRareModifierManager(): RarePersonaModificationManager = parseFile()
}