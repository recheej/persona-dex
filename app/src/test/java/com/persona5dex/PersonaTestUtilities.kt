package com.persona5dex

import android.util.JsonReader
import com.persona5dex.models.Enumerations
import com.persona5dex.models.GameType.Companion.toGameType
import com.persona5dex.models.PersonaForFusionService
import java.io.File

fun Any.getFile(path: String) = File(javaClass.classLoader!!.getResource(path).file)

fun Any.getFusionPersonas(): List<PersonaForFusionService> {
    val file = getFile("personas.json")

    return JsonReader(file.bufferedReader()).run {
        use {
            val personasForFusion = mutableListOf<PersonaForFusionService>()

            beginArray()
            while (hasNext()) {
                beginArray()
                val persona = PersonaForFusionService().apply {
                    id = nextInt()
                    setName(nextString())
                    arcana = Enumerations.Arcana.getArcana(nextInt())
                    level = nextInt()
                    setGameId(nextInt().toGameType())
                    isRare = nextInt() == 1
                    isDlc = nextInt() == 1
                    isParty = nextInt() == 1
                }
                personasForFusion.add(persona)
                endArray()
            }
            endArray()

            personasForFusion
        }
    }
}