package com.persona5dex.fusionService

interface PersonaRareModifierService {
    suspend fun getRareModifierManager(): RarePersonaModificationManager
}