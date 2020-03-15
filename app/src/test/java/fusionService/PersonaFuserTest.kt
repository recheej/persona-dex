package fusionService

import android.os.Build
import com.persona5dex.*
import com.persona5dex.fusionService.FusionChartServiceFactory
import com.persona5dex.models.GameType
import com.persona5dex.repositories.PersonaFusions
import com.persona5dex.services.PersonaFuserV2
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
    private lateinit var personaFusions: PersonaFusions

    @Before
    fun setup() {
        application = RuntimeEnvironment.application as Persona5Application
        arcanaNameProvider = ArcanaNameProvider(application)

        fusionChartFactory = FusionChartServiceFactory(application, arcanaNameProvider)
    }

    @Test
    fun testBasicFusion() = runBlocking {

        val fusionChart = fusionChartFactory.getFusionChartService(gameType).getFusionChart()

        val allPersonas = getFusionPersonas(arcanaNameProvider).filter { it.gameType == gameType }

        personaFusions = PersonaFusions(allPersonas, allPersonas.filter { it.isDlc }.toSet())
        personaFuser = PersonaFuserV2(personaFusions, fusionChart)

        val resultPersona = personaOne fuse personaTwo

        Assert.assertEquals(expectedResultPersonaName?.normalizeName(), resultPersona?.name?.normalizeName())
    }

    private fun String.findPersona() =
            try {
                personaFusions.allPersonas.first { it.name equalNormalized this }
            } catch (e: NoSuchElementException) {
                throw IllegalStateException("failed to find persona: $this", e)
            }

    private infix fun String.fuse(other: String) =
            personaFuser.fusePersona(findPersona(), other.findPersona())

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{index}: testFusion({0} + {1} = {2}. GameType={3}")
        fun data() = listOf(
                arrayOf("Jack Frost", "Hua Po", "Yaksini", GameType.BASE),
                arrayOf("Arsene", "Jack-o'-Lantern", "Mandrake", GameType.BASE),
                arrayOf("Hecatoncheires", "Hua Po", "Orthrus", GameType.BASE), //same arcana fusion
                arrayOf("Ariadne Picaro", "Succubus", "Mithras", GameType.BASE),
                arrayOf("Anubis", "Power", null, GameType.BASE)
        )
    }
}
