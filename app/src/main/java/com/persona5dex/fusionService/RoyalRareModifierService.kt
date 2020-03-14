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

class RoyalRareModifierService constructor(
        @Named("applicationContext") context: Context,
        val arcanaNameProvider: ArcanaNameProvider
) : PersonaRareFileModifierService(context) {

    override fun getFileRes(): Int = R.raw.royal_rare_combos

    override suspend fun parseFile(fileInputStream: InputStream): RarePersonaModificationManager =
            withContext(Dispatchers.IO) {
                JsonReader(InputStreamReader(fileInputStream, "UTF-8")).run {
                    use {

                        val rarePersonaMap = mutableMapOf<Arcana, Array<Int>>()
                        beginObject()

                        val rarePersonas = getRarePersonas()
                        val arcanas = getArcanas()

                        addToRarePersonaMap(rarePersonaMap, arcanas, rarePersonas.size)

                        endObject()

                        RarePersonaModificationManager(rarePersonaMap, rarePersonas)
                    }
                }
            }

    private fun JsonReader.addToRarePersonaMap(rarePersonaMap: MutableMap<Arcana, Array<Int>>, arcanas: Array<Arcana>, personaCount: Int) {
        check(nextName() == "table")
        beginArray()

        var index = 0
        while (hasNext()) {
            beginArray()

            val arcana = arcanas[index]

            var personaIndex = 0
            while (hasNext()) {
                rarePersonaMap.getOrPut(arcana) {
                    IntArray(personaCount).toTypedArray()
                }[personaIndex] = nextInt()
                personaIndex++
            }

            endArray()
            index++
        }
        endArray()
    }

    private fun JsonReader.getArcanas(): Array<Arcana> {
        check(nextName() == "races")

        val arcanas = mutableListOf<Arcana>()
        beginArray()
        while (hasNext()) {
            arcanas.add(arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString()))
        }
        endArray()
        return arcanas.toTypedArray()
    }

    private fun JsonReader.getRarePersonas(): Array<String> {
        check(nextName() == "elems")
        beginArray()

        val rarePersonas = mutableListOf<String>()
        while (hasNext()) {
            rarePersonas.add(nextString())
        }
        endArray()
        return rarePersonas.toTypedArray()
    }
}