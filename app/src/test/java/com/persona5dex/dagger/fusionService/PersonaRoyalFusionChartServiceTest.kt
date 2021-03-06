package com.persona5dex.dagger.fusionService

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.fusionService.PersonaRoyalFusionChartService
import com.persona5dex.models.Enumerations.Arcana
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class PersonaRoyalFusionChartServiceTest {

    private lateinit var arcanaNameProvider: ArcanaNameProvider
    private lateinit var context: Context

    private lateinit var chartService: PersonaRoyalFusionChartService

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        arcanaNameProvider = ArcanaNameProvider(context)

        chartService = PersonaRoyalFusionChartService(context, arcanaNameProvider)
    }

    @Test
    fun `getFusionChart() returns valid fusion chart`() {
        runBlocking {
            val chart = chartService.getFusionChart()
            val resultArcana = chart.getResultArcana(Arcana.MAGICIAN, Arcana.FOOL)
            Assert.assertEquals(Arcana.DEATH, resultArcana)
        }
    }

    @Test
    fun `getFusionChart() returns valid fusion chart for hanged man`() {
        runBlocking {
            val chart = chartService.getFusionChart()
            val resultArcana = chart.getResultArcana(Arcana.HANGED_MAN, Arcana.MAGICIAN)
            Assert.assertEquals(Arcana.EMPRESS, resultArcana)
        }
    }
}
