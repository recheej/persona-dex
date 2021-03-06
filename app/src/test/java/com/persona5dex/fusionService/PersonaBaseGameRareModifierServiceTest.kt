package com.persona5dex.fusionService

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.persona5dex.ArcanaNameProvider
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
class PersonaBaseGameRareModifierServiceTest {

    private lateinit var arcanaNameProvider: ArcanaNameProvider
    private lateinit var context: Context

    private lateinit var modifierService: BaseGameRareModifierService

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        arcanaNameProvider = ArcanaNameProvider(context)

        modifierService = BaseGameRareModifierService(context, arcanaNameProvider)
    }

    @Test
    fun `getRareModifierManager() returns valid manager`() {
        runBlocking {
            val manager = modifierService.getRareModifierManager()
            val modifier = manager.getModifier(Arcana.FOOL, "Queens Necklace")
            Assert.assertEquals(1, modifier)
        }
    }

    @Test
    fun `getRareModifierManager() returns valid manager for hanged man`() {
        runBlocking {
            val manager = modifierService.getRareModifierManager()
            val modifier = manager.getModifier(Arcana.HANGED_MAN, "Queens Necklace")
            Assert.assertEquals(1, modifier)
        }
    }
}
