package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.R
import com.persona5dex.models.Enumerations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named

class AdvancedPersonaFusionsFileService @Inject constructor(@Named("applicationContext") context: Context) : PersonaJsonFileService<List<AdvancedPersonaFusion>>(context) {
    override fun getFileRes(): Int = R.raw.advanced_fusions

    override suspend fun parseJson(jsonReader: JsonReader): List<AdvancedPersonaFusion> {
        return jsonReader.run {
            beginArray()
            val advancedFusions = mutableListOf<AdvancedPersonaFusion>()
            while (hasNext()) {
                advancedFusions.add(parseFusion(jsonReader))
            }
            endArray()

            advancedFusions
        }
    }

    private fun parseFusion(jsonReader: JsonReader): AdvancedPersonaFusion {
        return jsonReader.run {
            beginObject()
            check(nextName() == "result")
            val name = nextString()
            check(nextName() == "sources")
            val sources = getSources(jsonReader)

            endObject()

            AdvancedPersonaFusion(name, sources)
        }
    }

    private fun getSources(jsonReader: JsonReader): List<String> {
        return jsonReader.run {
            val sources = mutableListOf<String>()
            beginArray()
            while (hasNext()) {
                sources.add(nextString())
            }
            endArray()

            sources
        }
    }
}

data class AdvancedPersonaFusion(
        val resultPersonaName: String,
        val sourcePersonaNames: List<String>
)