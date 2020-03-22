package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.R
import com.persona5dex.models.Enumerations
import javax.inject.Inject
import javax.inject.Named


class PersonaRoyalFusionChartService @Inject constructor(
        @Named("applicationContext") context: Context,
        private val arcanaNameProvider: ArcanaNameProvider
) : PersonaFileFusionChartService(context) {

    override suspend fun parseJson(jsonReader: JsonReader): FusionChart {
        jsonReader.beginObject()
        check(jsonReader.nextName() == "races")
        jsonReader.beginArray()
        var arcanaIndex = 0

        val arcanaIndexMap = mutableMapOf<Int, Enumerations.Arcana>()
        while (jsonReader.hasNext()) {
            val arcana = checkNotNull(arcanaNameProvider.getArcanaForEnglishName(jsonReader.nextString()))
            arcanaIndexMap[arcanaIndex++] = arcana
        }
        jsonReader.endArray()

        check(jsonReader.nextName() == "table")
        val tableArray = jsonReader.getTableArray()
        jsonReader.endObject()


        val fusionChartMap = mutableMapOf<Enumerations.Arcana, MutableMap<Enumerations.Arcana, Enumerations.Arcana>>()
        tableArray.forEachIndexed { row, arcanas ->
            val arcanaForRow = arcanaIndexMap.getValue(row)
            arcanas.forEachIndexed { col, arcana ->
                val arcanaForColumn = arcanaIndexMap.getValue(col)
                fusionChartMap.getOrPut(arcanaForColumn) {
                    mutableMapOf()
                }[arcanaForRow] = arcana ?: arcanaForRow

                fusionChartMap.getOrPut(arcanaForRow) {
                    mutableMapOf()
                }[arcanaForColumn] = arcana ?: arcanaForColumn
            }
        }

        return FusionChart(fusionChartMap)
    }

    override fun getFileRes(): Int = R.raw.arcana_combo_royal


    private fun JsonReader.getTableArray(): List<List<Enumerations.Arcana?>> {
        val fusionChartList = mutableListOf<List<Enumerations.Arcana?>>()
        beginArray()
        while (hasNext()) {
            fusionChartList.add(getFusionRow())
        }
        endArray()
        return fusionChartList
    }

    private fun JsonReader.getFusionRow(): List<Enumerations.Arcana?> {
        val fusionRow = mutableListOf<Enumerations.Arcana?>()

        beginArray()
        while (hasNext()) {
            val arcana = arcanaNameProvider.getArcanaForEnglishName(nextString())
            fusionRow.add(arcana)
        }
        endArray()
        return fusionRow
    }
}


