package com.persona5dex.fusionService.advanced

import android.content.Context
import android.util.JsonReader
import com.persona5dex.R
import javax.inject.Inject
import javax.inject.Named

class AdvancedPersonaFusionsFileService @Inject constructor(@Named("applicationContext") context: Context) : AdvancedPersonaJsonFileService(context) {
    override fun getFileRes(): Int = R.raw.advanced_fusions

    override suspend fun parseJson(jsonReader: JsonReader): List<AdvancedPersonaFusion> =
            jsonReader.run {
                beginArray()
                val advancedFusions = mutableListOf<AdvancedPersonaFusion>()
                while (hasNext()) {
                    advancedFusions.add(parseFusion(jsonReader))
                }
                endArray()

                advancedFusions
            }

    private fun parseFusion(jsonReader: JsonReader): AdvancedPersonaFusion =
            jsonReader.run {
                beginObject()
                check(nextName() == "result")
                val name = nextString()
                check(nextName() == "sources")
                val sources = getSources(jsonReader)

                endObject()

                AdvancedPersonaFusion(name, sources)
            }

    private fun getSources(jsonReader: JsonReader): List<String> =
            jsonReader.run {
                val sources = mutableListOf<String>()
                beginArray()
                while (hasNext()) {
                    sources.add(nextString())
                }
                endArray()

                sources
            }
}

