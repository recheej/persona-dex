@file:JvmName("PersonaFilters")

package com.persona5dex

import android.os.Debug
import com.persona5dex.models.GameType
import com.persona5dex.models.GameTypePersona


fun <T : GameTypePersona> List<T>.filterGameType(gameType: GameType): List<T> {
    val basePersonas = filter { it.gameType == GameType.BASE }
    return if (gameType == GameType.BASE) {
        basePersonas
    } else {
        Debug.startMethodTracing("demo.trace")
        val personasForGameType = filter { it.gameType == gameType }
        val personasForGameNameSet = personasForGameType.map { it.name }.toSet()
        val allGameTypePersonas = basePersonas + personasForGameType
        val result = allGameTypePersonas
                .filterNot { it.gameType == GameType.BASE && personasForGameNameSet.contains(it.name) }
        Debug.stopMethodTracing()
        result
    }
}