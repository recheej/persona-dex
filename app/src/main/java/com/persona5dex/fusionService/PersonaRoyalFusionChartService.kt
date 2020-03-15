package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.R
import com.persona5dex.models.Enumerations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named


class PersonaRoyalFusionChartService @Inject constructor(
        @Named("applicationContext") context: Context,
        private val arcanaNameProvider: ArcanaNameProvider
) : PersonaFileFusionChartService(context) {
    override fun getFileRes(): Int = R.raw.arcana_combo_royal

    override suspend fun parseFile(fileInputStream: InputStream): FusionChart = withContext(Dispatchers.IO) {
        val reader = JsonReader(InputStreamReader(fileInputStream, "UTF-8"))
        reader.use {
            reader.beginObject()
            check(reader.nextName() == "races")
            reader.beginArray()
            var arcanaIndex = 0

            val arcanaIndexMap = mutableMapOf<Int, Enumerations.Arcana>()
            while (reader.hasNext()) {
                val arcana = checkNotNull(arcanaNameProvider.getArcanaForEnglishName(reader.nextString()))
                arcanaIndexMap[arcanaIndex++] = arcana
            }
            reader.endArray()

            check(reader.nextName() == "table")
            val tableArray = reader.getTableArray()
            reader.endObject()


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
                    }[arcanaForColumn] = arcana ?: arcanaForRow
                }
            }

            FusionChart(fusionChartMap)
        }
    }

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


