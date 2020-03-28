package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.R
import javax.inject.Inject
import javax.inject.Named

class AdvancedPersonaRoyalFusionsFileService @Inject constructor(@Named("applicationContext") context: Context) : PersonaJsonFileService<List<AdvancedPersonaFusion>>(context) {
    override fun getFileRes(): Int = R.raw.advanced_fusions_royal

    override suspend fun parseJson(jsonReader: JsonReader): List<AdvancedPersonaFusion> {
        return jsonReader.run {
            beginObject()
            val advancedFusions = mutableListOf<AdvancedPersonaFusion>()
            while (hasNext()) {
                advancedFusions.add(parseFusion(jsonReader))
            }
            endObject()

            advancedFusions
        }
    }

    private fun parseFusion(jsonReader: JsonReader): AdvancedPersonaFusion {
        return jsonReader.run {
            val resultName = nextName()
            val sources = getSources(jsonReader)

            AdvancedPersonaFusion(resultName, sources)
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
