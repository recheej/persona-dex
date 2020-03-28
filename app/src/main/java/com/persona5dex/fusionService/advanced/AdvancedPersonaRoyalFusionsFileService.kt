package com.persona5dex.fusionService.advanced

import android.content.Context
import android.util.JsonReader
import com.persona5dex.R
import javax.inject.Inject
import javax.inject.Named

class AdvancedPersonaRoyalFusionsFileService @Inject constructor(@Named("applicationContext") context: Context) : AdvancedPersonaJsonFileService(context) {
    override fun getFileRes(): Int = R.raw.advanced_fusions_royal

    override suspend fun parseJson(jsonReader: JsonReader): List<AdvancedPersonaFusion> {
        return jsonReader.run {
            val advancedPersonas = mutableListOf<AdvancedPersonaFusion>()
            beginObject()
            while (hasNext()) {
                advancedPersonas.add(parseFusion(jsonReader))
            }
            endObject()
            advancedPersonas
        }
    }

    private fun parseFusion(jsonReader: JsonReader) =
            jsonReader.run {
                val resultName = nextName()
                val sources = getSources(jsonReader)

                AdvancedPersonaFusion(resultName, sources)
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
