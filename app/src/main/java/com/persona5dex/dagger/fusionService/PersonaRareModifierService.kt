package com.persona5dex.dagger.fusionService

interface PersonaRareModifierService {
    suspend fun getRareModifierManager(): RarePersonaModificationManager
}