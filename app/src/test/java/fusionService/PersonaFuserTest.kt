package fusionService

import android.os.Build
import com.persona5dex.*
import com.persona5dex.fusionService.FusionChartServiceFactory
import com.persona5dex.models.GameType
import com.persona5dex.repositories.PersonaFusionRepository
import com.persona5dex.repositories.PersonaFusions
import com.persona5dex.services.PersonaFuserV2
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
class PersonaFuserTest {

    private lateinit var arcanaNameProvider: ArcanaNameProvider
    private lateinit var application: Persona5Application
    private lateinit var fusionChartFactory: FusionChartServiceFactory
    private lateinit var personaFuser: PersonaFuserV2

    private lateinit var personaFusions: PersonaFusions

    private val baseGameFusionChartService by lazy {
        fusionChartFactory.getFusionChartService(GameType.BASE)
    }

    @Before
    fun setup() = runBlocking {
        javaClass.classLoader
        application = RuntimeEnvironment.application as Persona5Application
        arcanaNameProvider = ArcanaNameProvider(application)

        fusionChartFactory = FusionChartServiceFactory(application, arcanaNameProvider)

        val allPersonas = getFusionPersonas(arcanaNameProvider).filter { it.gameType == GameType.BASE }
        personaFusions = PersonaFusions(allPersonas, emptyList())
        personaFuser = PersonaFuserV2(personaFusions, baseGameFusionChartService.getFusionChart())
    }

    @Test
    fun `testBasicFusion`() {
        val resultPersona = "Arsene" fuse "Jack-o'-Lantern"

        Assert.assertEquals("Mandrake".normalizeName(), resultPersona!!.name.normalizeName())
    }

    private fun String.findPersona() =
            personaFusions.allPersonas.first { it.name equalNormalized this }

    private infix fun String.fuse(other: String) =
            personaFuser.fusePersona(findPersona(), other.findPersona())


}
