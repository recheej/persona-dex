@file:JvmName("PersonaFilters")

package com.persona5dex

import com.persona5dex.models.GameType
import com.persona5dex.models.GameTypePersona


fun <T : GameTypePersona> List<T>.filterGameType(gameType: GameType): List<T> {
    val basePersonas = filter { it.gameId == GameType.BASE }
    return if (gameType == GameType.BASE) {
        basePersonas
    } else {
        val personasForGameType = filter { it.gameId == gameType }
        val personasForGameNameSet = personasForGameType.map { it.name }.toSet()
        val allGameTypePersonas = basePersonas + personasForGameType
        val result = allGameTypePersonas
                .filterNot { it.gameId == GameType.BASE && personasForGameNameSet.contains(it.name) }
        result
    }
}

fun <T : GameTypePersona> List<T>.filterGameType(gameType: GameType, basePersonas: Boolean, royalPersonas: Boolean): List<T> {
    return if ((!royalPersonas && !basePersonas) || (gameType == GameType.BASE && !basePersonas)) {
        emptyList()
    } else {
        filterGameType(gameType).filter {
            if (gameType == GameType.BASE) basePersonas else royalPersonas
        }
    }
}