package fusionService

import android.content.SharedPreferences
import android.os.Build
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.Persona5Application
import com.persona5dex.equalNormalized
import com.persona5dex.fusionService.FusionChartServiceFactory
import com.persona5dex.getFusionPersonas
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.models.room.PersonaDao
import com.persona5dex.repositories.PersonaFusionRepository
import com.persona5dex.repositories.PersonaFusionRepository.Companion.DLC_SHARED_PREF
import com.persona5dex.fusionService.PersonaFuserV2
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml", sdk = [Build.VERSION_CODES.O])
class PersonaFuserTest(
        private val personaOne: String,
        private val personaTwo: String,
        private val expectedResultPersonaName: String?,
        private val gameType: GameType
) {

    private lateinit var arcanaNameProvider: ArcanaNameProvider
    private lateinit var application: Persona5Application
    private lateinit var fusionChartFactory: FusionChartServiceFactory
    private lateinit var personaFuser: PersonaFuserV2
    private lateinit var allPersonas: List<PersonaForFusionService>

    @Before
    fun setup() {
        application = RuntimeEnvironment.application as Persona5Application
        arcanaNameProvider = ArcanaNameProvider(application)

        fusionChartFactory = FusionChartServiceFactory(application, arcanaNameProvider)
    }

    @Test
    fun testBasicFusion() = runBlocking {

        val fusionChart = fusionChartFactory.getFusionChartService(gameType).getFusionChart()

        allPersonas = getFusionPersonas()

        val personaDao: PersonaDao = mock()
        whenever(personaDao.personasByLevel).thenReturn(allPersonas.toTypedArray())

        val mockPreferences: SharedPreferences = mock()

        val dlcPersonaSet = allPersonas
                .filter { it.isDlc }
                .map { it.id.toString() }
                .toSet()

        whenever(mockPreferences.getStringSet(eq(DLC_SHARED_PREF), any())).thenReturn(dlcPersonaSet)

        val fusionRepository = PersonaFusionRepository(personaDao, mockPreferences, gameType)

        personaFuser = PersonaFuserV2(fusionRepository, fusionChart)

        val resultPersona = personaOne fuse personaTwo

        Assert.assertEquals(expectedResultPersonaName, resultPersona?.name)
    }

    private fun String.findPersona(gameType: GameType) =
            try {
                allPersonas.firstOrNull { it.gameType == gameType && it.name equalNormalized this }
                        ?: allPersonas.first { it.gameType == GameType.BASE && it.name equalNormalized this }
            } catch (e: NoSuchElementException) {
                throw IllegalStateException("failed to find persona: $this", e)
            }

    private suspend infix fun String.fuse(other: String) =
            personaFuser.fusePersona(findPersona(gameType), other.findPersona(gameType))

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
