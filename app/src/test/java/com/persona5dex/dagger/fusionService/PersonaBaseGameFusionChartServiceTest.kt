package com.persona5dex.dagger.fusionService

import android.content.Context
import android.os.Build
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.fusionService.PersonaBaseGameFusionChartService
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
@Config(manifest = "src/main/AndroidManifest.xml", sdk = [Build.VERSION_CODES.O])
class PersonaBaseGameFusionChartServiceTest {

    private lateinit var arcanaNameProvider: ArcanaNameProvider
    private lateinit var context: Context

    private lateinit var chartService: PersonaBaseGameFusionChartService

    @Before
    fun setup() {
        context = RuntimeEnvironment.application
        arcanaNameProvider = ArcanaNameProvider(context)

        chartService = PersonaBaseGameFusionChartService(context, arcanaNameProvider)
    }

    @Test
    fun `getFusionChart() returns valid fusion chart`() {
        runBlocking {
            val chart = chartService.getFusionChart()
            val resultArcana = chart.getResultArcana(Arcana.MAGICIAN, Arcana.PRIESTESS)
            Assert.assertEquals(Arcana.TEMPERANCE, resultArcana)
        }
    }

    @Test
    fun `getFusionChart() returns null`() {
        runBlocking {
            val chart = chartService.getFusionChart()
            val resultArcana = chart.getResultArcana(Arcana.MAGICIAN, Arcana.FOOL)
            Assert.assertNull(resultArcana)
        }
    }

    @Test
    fun `getFusionChart() returns valid fusion chart for hanged man`() {
        runBlocking {
            val chart = chartService.getFusionChart()
            val resultArcana = chart.getResultArcana(Arcana.HANGED_MAN, Arcana.DEATH)
            Assert.assertEquals(Arcana.MOON, resultArcana)
        }
    }
}
