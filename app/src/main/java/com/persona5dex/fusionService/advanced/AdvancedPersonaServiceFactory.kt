package com.persona5dex.fusionService.advanced

import android.content.Context
import com.persona5dex.models.GameType
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AdvancedPersonaServiceFactory @Inject constructor(
        @Named("applicationContext") private val context: Context
) {
    private val baseService by lazy { AdvancedPersonaFusionsFileService(context) }
    private val royalService by lazy { AdvancedPersonaRoyalFusionsFileService(context) }
    fun getAdvancedFusionService(gameType: GameType): AdvancedPersonaService =
            when (gameType) {
                GameType.BASE -> baseService
                GameType.ROYAL -> royalService
            }
}