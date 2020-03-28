package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.R
import com.persona5dex.models.Enumerations.Arcana
import javax.inject.Inject
import javax.inject.Named


class PersonaBaseGameFusionChartService @Inject constructor(
        @Named("applicationContext") context: Context,
        private val arcanaNameProvider: ArcanaNameProvider
) : PersonaFileFusionChartService(context) {
    override fun getFileRes(): Int = R.raw.arcana_combo_data

    override suspend fun parseJson(jsonReader: JsonReader): FusionChart =
            jsonReader.run {
                beginArray()
                val fusionMap = mutableMapOf<Arcana, MutableMap<Arcana, Arcana>>()
                while (hasNext()) {
                    addArcanaMapEntry(fusionMap)
                }
                endArray()

                FusionChart(fusionMap)
            }

    private fun JsonReader.addArcanaMapEntry(fusionMap: MutableMap<Arcana, MutableMap<Arcana, Arcana>>) {
        beginObject()

        check(nextName() == "source")

        beginArray()
        val arcanaOne = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())
        val arcanaTwo = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())
        endArray()

        check(nextName() == "result")

        val resultArcana = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())
        fusionMap.getOrPut(arcanaOne) {
            mutableMapOf()
        }[arcanaTwo] = resultArcana

        fusionMap.getOrPut(arcanaTwo) {
            mutableMapOf()
        }[arcanaOne] = resultArcana

        endObject()
    }
}


