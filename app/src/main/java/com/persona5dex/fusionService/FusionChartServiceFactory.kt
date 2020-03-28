package com.persona5dex.fusionService

import android.content.Context
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.models.GameType
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FusionChartServiceFactory @Inject constructor(
        @Named("applicationContext") private val context: Context,
        private val arcanaNameProvider: ArcanaNameProvider
) {
    private val baseGameFusionChartService by lazy { PersonaBaseGameFusionChartService(context, arcanaNameProvider) }
    private val royalFusionChartService by lazy { PersonaRoyalFusionChartService(context, arcanaNameProvider) }
    fun getFusionChartService(gameType: GameType): FusionChartService =
            when (gameType) {
                GameType.BASE -> baseGameFusionChartService
                GameType.ROYAL -> royalFusionChartService
            }
}