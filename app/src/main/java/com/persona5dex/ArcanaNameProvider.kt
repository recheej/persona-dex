package com.persona5dex

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.persona5dex.dagger.application.ApplicationScope
import com.persona5dex.models.Enumerations
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ApplicationScope
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
            rawEnglishArcanaName.normalizeName().let { inputNormalizedName ->
                englishArcanaMap.entries.firstOrNull {
                    it.value.normalizeName() == inputNormalizedName
                }?.key
                        ?: Enumerations.Arcana.HANGED_MAN.takeIf { inputNormalizedName.contains("hanged") }
            }

    fun getArcanaForEnglishNameOrThrow(rawEnglishArcanaName: String): Enumerations.Arcana =
            checkNotNull(getArcanaForEnglishName(rawEnglishArcanaName)) {
                "could not find arcana for name: $rawEnglishArcanaName"
            }

    data class ArcanaName(val arcana: Enumerations.Arcana, val arcanaName: String) {
        override fun toString(): String = arcanaName
    }

    private fun getUSResource(): Resources {
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(Locale.US)
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources
    }
}

