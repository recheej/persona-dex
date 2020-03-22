package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.R
import com.persona5dex.models.Enumerations.Arcana
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Named

class BaseGameRareModifierService constructor(
        @Named("applicationContext") context: Context,
        val arcanaNameProvider: ArcanaNameProvider
) : PersonaRareFileModifierService(context) {

    override fun getFileRes(): Int = R.raw.rare_combos

    override suspend fun parseJson(jsonReader: JsonReader): RarePersonaModificationManager =
            jsonReader.run {

                val rarePersonaMap = mutableMapOf<Arcana, Array<Int>>()
                beginArray()
                while (hasNext()) {
                    addToRarePersonaMap(rarePersonaMap)
                }
                endArray()

                RarePersonaModificationManager(rarePersonaMap, RARE_PERSONAS)
            }

    private fun JsonReader.addToRarePersonaMap(rarePersonaMap: MutableMap<Arcana, Array<Int>>) {
        beginObject()
        check(nextName() == "name")
        val arcana = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())

        check(nextName() == "modifiers")

        rarePersonaMap[arcana] = getModifiers()
        endObject()
    }

    private fun JsonReader.getModifiers(): Array<Int> {
        val modifiers = mutableListOf<Int>()
        beginArray()
        while (hasNext()) {
            modifiers.add(nextInt())
        }
        endArray()
        return modifiers.toTypedArray()
    }

    private companion object {
        private val RARE_PERSONAS = arrayOf("Regent", "Queen's Necklace", "Stone of Scone",
                "Koh-i-Noor", "Orlov", "Emperor's Amulet", "Hope Diamond", "Crystal Skull")
    }
}