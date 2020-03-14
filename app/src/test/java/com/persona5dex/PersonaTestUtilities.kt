package com.persona5dex

import android.util.JsonReader
import com.persona5dex.models.Enumerations
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import java.io.File

fun Any.getFile(path: String) = File(javaClass.classLoader!!.getResource(path).file)

fun Any.getFusionPersonas(arcanaNameProvider: ArcanaNameProvider): List<PersonaForFusionService> {
    val file = getFile("personas.json")
    fun JsonReader.skipValues() {
        skipValue()
        skipValue()
        skipValue()
        skipValue()
        skipValue()
        skipValue()
    }

    return JsonReader(file.bufferedReader()).run {
        use {
            beginObject()
            val personasForFusion = mutableListOf<PersonaForFusionService>()
            skipValues()
            check(nextName() == "rows")
            beginArray()
            while (hasNext()) {
                beginArray()
                val persona = PersonaForFusionService().apply {
                    arcana = Enumerations.Arcana.getArcana(nextInt())
                    name = nextString()
                    level = nextInt()
                    gameType = GameType.getGameType(nextInt())
                }
                personasForFusion.add(persona)
                endArray()
            }
            endArray()

            endObject()

            personasForFusion
        }
    }
}