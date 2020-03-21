package com.persona5dex

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.persona5dex.dagger.application.ApplicationScope
import com.persona5dex.extensions.equalNormalized
import com.persona5dex.extensions.normalize
import com.persona5dex.models.Enumerations
import com.persona5dex.models.Enumerations.Arcana
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.Comparator

@ApplicationScope
class ArcanaNameProvider @Inject constructor(@Named("applicationContext") private val context: Context) {

    private val englishArcanaMap = getUSResource().let {
        Arcana.values().associate { arcana ->
            arcana to it.getString(arcana.textRes)
        }
    }

    private val arcanaComparator = ArcanaComparator()
    private val arcanaNameComparator = ArcanaNameComparator(arcanaComparator)

    fun getEnglishArcanaName(arcana: Arcana): String =
            englishArcanaMap[arcana] ?: error("cannot find english name for arcana: $this")

    fun getArcanaNameForDisplay(arcana: Arcana) =
            context.getString(arcana.textRes)

    fun getAllArcanaNamesForDisplay() =
            Arcana.values()
                    .map { ArcanaName(it, getArcanaNameForDisplay(it)) }
                    .toTypedArray()
                    .sortedWith(arcanaNameComparator)

    fun getArcanaForEnglishName(rawEnglishArcanaName: String) =
            englishArcanaMap.entries.firstOrNull {
                it.value equalNormalized rawEnglishArcanaName
            }?.key
                    ?: Arcana.HANGED_MAN.takeIf { rawEnglishArcanaName.normalize().contains("hanged") }

    fun getArcanaForEnglishNameOrThrow(rawEnglishArcanaName: String): Arcana =
            checkNotNull(getArcanaForEnglishName(rawEnglishArcanaName)) {
                "could not find arcana for name: $rawEnglishArcanaName"
            }

    data class ArcanaName(val arcana: Arcana, val arcanaName: String) {
        override fun toString(): String = arcanaName
    }

    private fun getUSResource(): Resources {
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(Locale.US)
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources
    }

    private inner class ArcanaComparator : Comparator<Arcana> {
        override fun compare(o1: Arcana?, o2: Arcana?): Int {
            return when {
                o1 == null && o2 == null -> 0
                o1 == null -> -1
                o2 == null -> 1
                o1 == Arcana.ANY && o2 == Arcana.ANY -> 0
                o1 == Arcana.ANY -> -1
                o2 == Arcana.ANY -> 1
                else -> getArcanaNameForDisplay(o1).compareTo(getArcanaNameForDisplay(o2))
            }
        }
    }

    private class ArcanaNameComparator(private val arcanaComparator: ArcanaComparator) : Comparator<ArcanaName> {
        override fun compare(o1: ArcanaName?, o2: ArcanaName?): Int =
                arcanaComparator.compare(o1?.arcana, o2?.arcana)
    }
}

