package com.persona5dex.dagger.fusionService

import android.content.Context
import android.os.Build
import android.util.Log
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.BuildConfig
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml", sdk = [Build.VERSION_CODES.O])
class PersonaRoyalFusionChartServiceTest {

    private lateinit var arcanaNameProvider: ArcanaNameProvider
    private lateinit var context: Context

    private lateinit var chartService: PersonaRoyalFusionChartService

    @Before
    fun setup() {
        context = RuntimeEnvironment.application
        arcanaNameProvider = ArcanaNameProvider(context)

        chartService = PersonaRoyalFusionChartService(context, arcanaNameProvider)
    }

    @Test
    fun `testGetFusionChart`() {
        runBlocking {
            val chart = chartService.getFusionChart()
            Assert.assertNotNull(chart)
        }
    }
}
