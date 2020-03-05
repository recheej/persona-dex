package com.persona5dex

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.persona5dex.models.Enumerations
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ArcanaNameProvider @Inject constructor(@Named("applicationContext") private val context: Context) {

    private val englishArcanaMap = getUSResource().let {
        Enumerations.Arcana.values().associate { arcana ->
            arcana to it.getString(arcana.textRes)
        }
    }

    fun Enumerations.Arcana.getEnglishArcanaName() =
            englishArcanaMap[this] ?: error("cannot find english name for arcana: $this")

    @JvmName("getArcanaNameForDisplay")
    fun Enumerations.Arcana.getNameForDisplay() =
            context.getString(textRes)

    fun getAllArcanaNames() =
            Enumerations.Arcana.values().map { ArcanaName(it, it.getNameForDisplay()) }.toTypedArray()

    fun getArcanaForEnglishName(rawEnglishArcanaName: String) =
            rawEnglishArcanaName.normalizeArcanaName().let { inputNormalizedName ->
                englishArcanaMap.entries.firstOrNull {
                    it.value.normalizeArcanaName() == inputNormalizedName
                }?.key
            }

    private fun String.normalizeArcanaName(): String =
            replace("\\s+".toRegex(), "")
                    .replace("_".toRegex(), "").toLowerCase(Locale.US)

    data class ArcanaName(val arcana: Enumerations.Arcana, val arcanaName: String)

    private fun getUSResource(): Resources {
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(Locale.US)
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources
    }
}