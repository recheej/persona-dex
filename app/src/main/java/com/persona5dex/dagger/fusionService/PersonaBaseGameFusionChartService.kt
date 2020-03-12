package com.persona5dex.dagger.fusionService

import android.content.Context
import android.util.JsonReader
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.R
import com.persona5dex.models.Enumerations.Arcana
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named


class PersonaBaseGameFusionChartService @Inject constructor(
        @Named("applicationContext") context: Context,
        private val arcanaNameProvider: ArcanaNameProvider
) : PersonaFileFusionChartService(context) {
    override fun getFileRes(): Int = R.raw.arcana_combo_data

    override suspend fun parseFile(fileInputStream: InputStream): FusionChart =
            withContext(Dispatchers.IO) {
                JsonReader(InputStreamReader(fileInputStream, "UTF-8")).run {
                    use {
                        beginArray()
                        val fusionMap = mutableMapOf<Arcana, MutableMap<Arcana, Arcana>>()
                        while (hasNext()) {
                            addArcanaMapEntry(fusionMap)
                        }
                        endArray()

                        FusionChart(fusionMap)
                    }
                }

            }

    private fun JsonReader.addArcanaMapEntry(fusionMap: MutableMap<Arcana, MutableMap<Arcana, Arcana>>) {
        beginObject()

        check(nextName() == "source")

        beginArray()
        val arcanaOne = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())
        val arcanaTwo = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())
        endArray()

        check(nextName() == "result")

        fusionMap.getOrPut(arcanaOne) {
            mutableMapOf()
        }[arcanaTwo] = arcanaNameProvider.getArcanaForEnglishNameOrThrow(nextString())

        endObject()
    }
}


