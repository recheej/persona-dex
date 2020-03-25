package com.persona5dex.fusionService

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.*
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.getFusionPersonas
import com.persona5dex.models.GameType
import com.persona5dex.models.room.PersonaDao
import com.persona5dex.repositories.PersonaFusionRepository
import com.persona5dex.repositories.PersonaFusionRepository.Companion.DLC_SHARED_PREF
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
class PersonaFusionGraphGeneratorTest {

    private lateinit var personaFuser: PersonaFuserV2
    private lateinit var graphGenerator: PersonaFusionGraphGenerator

    @Before
    fun setup() = runBlocking {
        val allPersonas = getFusionPersonas()

        val personaDao: PersonaDao = mock()
        whenever(personaDao.personasByLevel).thenReturn(allPersonas.toTypedArray())

        val mockPreferences: SharedPreferences = mock()
        whenever(mockPreferences.getStringSet(eq(DLC_SHARED_PREF), any())).thenReturn(emptySet())

        val fusionRepository = PersonaFusionRepository(personaDao, mockPreferences, GameType.BASE)

        val context: Context = ApplicationProvider.getApplicationContext()
        val arcanaNameProvider = ArcanaNameProvider(context)

        val fusionChartFactory = FusionChartServiceFactory(context, arcanaNameProvider)

        val fusionChartService = fusionChartFactory.getFusionChartService(GameType.BASE)
        personaFuser = PersonaFuserV2(fusionRepository, fusionChartService.getFusionChart())

        graphGenerator = PersonaFusionGraphGenerator(fusionRepository, fusionChartService)
    }

    @Test
    fun testBasicGenerator() = runBlocking {
        val allFusions = graphGenerator.getAllFusions()
        Assert.assertTrue(allFusions.isNotEmpty())
    }

    @Test
    fun `testGetAllFusions doesn't have dups`() = runBlocking {
        val allFusions = graphGenerator.getAllFusions()
        val groups = allFusions.groupBy { it.hashCode() }
        val hasDuplicates = groups.any { it.value.count() > 1 }
        Assert.assertFalse(hasDuplicates)
    }
}
