package com.persona5dex.dagger.fusionService

import com.persona5dex.equalNormalized
import com.persona5dex.models.Enumerations.Arcana

class RarePersonaModificationManager(private val rarePersonaMap: Map<Arcana, Array<Int>>, private val rarePersonaList: Array<String>) {
    fun getModifier(normalPersonaArcana: Arcana, rarePersonaName: String): Int {
        val modifierList = checkNotNull(rarePersonaMap[normalPersonaArcana])
        val indexOfRarePersona = rarePersonaList.indexOfFirst { it equalNormalized rarePersonaName }
        check(indexOfRarePersona != -1) {
            "could not find rare persona: $rarePersonaName in list: $rarePersonaList"
        }

        return modifierList[indexOfRarePersona]
    }
}