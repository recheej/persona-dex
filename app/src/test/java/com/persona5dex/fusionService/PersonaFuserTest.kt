package com.persona5dex.fusionService

import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.Persona5Application
import com.persona5dex.extensions.equalNormalized
import com.persona5dex.getFusionPersonas
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.models.room.PersonaDao
import com.persona5dex.repositories.PersonaFusionRepository
import com.persona5dex.repositories.PersonaFusionRepository.Companion.DLC_SHARED_PREF
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RobolectricTestRunner

@RunWith(Enclosed::class)
class PersonaFuserTestSuiteTest {

    @RunWith(RobolectricTestRunner::class)
    abstract class Shared {
        private lateinit var arcanaNameProvider: ArcanaNameProvider
        private lateinit var application: Persona5Application
        private lateinit var fusionChartFactory: FusionChartServiceFactory
        protected lateinit var allPersonas: List<PersonaForFusionService>
        private val personaDao: PersonaDao = mock()
        private val mockPreferences: SharedPreferences = mock()

        @Before
        open fun setup() {
            application = ApplicationProvider.getApplicationContext() as Persona5Application
            arcanaNameProvider = ArcanaNameProvider(application)

            fusionChartFactory = FusionChartServiceFactory(application, arcanaNameProvider)

            allPersonas = getFusionPersonas()

            whenever(personaDao.personasByLevel).thenReturn(allPersonas.toTypedArray())

            val dlcPersonaSet = allPersonas
                    .filter { it.isDlc }
                    .map { it.id.toString() }
                    .toSet()

            whenever(mockPreferences.getStringSet(eq(DLC_SHARED_PREF), any())).thenReturn(dlcPersonaSet)
        }

        protected suspend fun GameType.getFuser() = runBlocking {
            fusionChartFactory.getFusionChartService(this@getFuser).getFusionChart().let {
                val fusionRepository = PersonaFusionRepository(personaDao, mockPreferences, this@getFuser)
                PersonaFuserV2(fusionRepository, it)
            }
        }

        protected fun String.findPersona(gameType: GameType) =
                try {
                    allPersonas.firstOrNull { it.gameId == gameType && it.name equalNormalized this }
                            ?: allPersonas.first { it.gameId == GameType.BASE && it.name equalNormalized this }
                } catch (e: NoSuchElementException) {
                    throw IllegalStateException("failed to find persona: $this", e)
                }

        protected suspend fun PersonaFuserV2.fusePersona(one: String, two: String, gameType: GameType): PersonaForFusionService? {
            return fusePersona(one.findPersona(gameType), two.findPersona(gameType))
        }

        protected infix fun PersonaForFusionService?.isEqual(other: String?) =
                Assert.assertTrue(namesEqual(other))

        protected infix fun PersonaForFusionService?.notEqual(other: String?) =
                Assert.assertFalse(namesEqual(other))

        private fun PersonaForFusionService?.namesEqual(other: String?) =
                this?.name equalNormalized other
    }

    @RunWith(ParameterizedRobolectricTestRunner::class)
    class PersonaFuserTest(
            private val personaOne: String,
            private val personaTwo: String,
            private val expectedResultPersonaName: String?,
            private val gameType: GameType
    ) : Shared() {

        private lateinit var personaFuser: PersonaFuserV2

        @Before
        override fun setup() = runBlocking {
            super.setup()
            personaFuser = gameType.getFuser()
        }

        @Test
        fun testBasicFusion() = runBlocking {
            val resultPersona = personaOne fuse personaTwo
            resultPersona isEqual expectedResultPersonaName
        }

        private suspend infix fun String.fuse(other: String) =
                personaFuser.fusePersona(this, other, gameType)

        companion object {
            @JvmStatic
            @ParameterizedRobolectricTestRunner.Parameters(name = "{index}: testFusion({0} + {1} = {2}. GameType={3}")
            fun data() = listOf(
                    arrayOf("Jack Frost", "Hua Po", "Yaksini", GameType.BASE),
                    arrayOf("Jack Frost", "Jack Frost", null, GameType.BASE),
                    arrayOf("Arsene", "Jack-o'-Lantern", "Mandrake", GameType.BASE),
                    arrayOf("Hecatoncheires", "Hua Po", "Orthrus", GameType.BASE), //same arcana fusion
                    arrayOf("Ariadne Picaro", "Succubus", "Mithras", GameType.BASE),
                    arrayOf("Anubis", "Power", null, GameType.BASE),
                    arrayOf("Cait Sith", "Naga", "Leanan Sidhe", GameType.ROYAL),
                    arrayOf("Biyarky", "Take-Minakata", "Yatagarasu", GameType.ROYAL),
                    arrayOf("Biyarky", "Phoenix", "Horus", GameType.ROYAL),
                    arrayOf("Biyarky", "Biyarky", null, GameType.ROYAL)
            )
        }
    }

    class SingleTests : Shared() {
        @Test
        fun `cannot fuse same arcana and get ingredient`() = runBlocking {
            val gameType = GameType.ROYAL
            val fuser = gameType.getFuser()
            val personaTwo = "cait sith"
            val result = fuser.fusePersona("jack frost", personaTwo, gameType)
            result notEqual personaTwo
        }

        @Test
        fun `fusion respects arcana ordering`() = runBlocking {
            val gameType = GameType.ROYAL
            val fuser = gameType.getFuser()
            val result = fuser.fusePersona("jack frost", "surt", gameType)
            result notEqual "cait sith"
        }

        @Test
        fun `fusion for royal ame-no-uzume`() = runBlocking {
            val gameType = GameType.ROYAL
            val fuser = gameType.getFuser()
            val result = fuser.fusePersona("angel", "hua po", gameType)
            result isEqual  "ame-no-uzume"
        }
    }

}
