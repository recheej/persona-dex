@file:JvmName("PersonaFilters")

package com.persona5dex

import com.persona5dex.models.GameType
import com.persona5dex.models.GameTypePersona
import com.persona5dex.models.MainListPersona


fun <T : GameTypePersona> List<T>.filterGameType(gameType: GameType): List<T> {
    val basePersonas = filter { it.gameId == GameType.BASE }
    return if (gameType == GameType.BASE) {
        basePersonas
    } else {
        val personasForGameType = filter { it.gameId == gameType }
        val personasForGameNameSet = personasForGameType.map { it.name }.toSet()
        val allGameTypePersonas = basePersonas + personasForGameType
        val result = allGameTypePersonas
                .filter { it.gameId == gameType || !personasForGameNameSet.contains(it.name) }
        result
    }
}

fun List<MainListPersona>.filterGameType(gameType: GameType, basePersonas: Boolean, royalPersonas: Boolean): List<MainListPersona> {
    return if ((!royalPersonas && !basePersonas) || (gameType == GameType.BASE && !basePersonas)) {
        emptyList()
    } else {
        when {
            basePersonas && royalPersonas -> filterGameType(gameType)
            basePersonas -> {
                val basePersonasSet = filter { it.gameId == GameType.BASE }.map { it.name }.toSet()
                val royalSet = filter { it.gameId == GameType.ROYAL }.map { it.name }.toSet()
                if (gameType == GameType.BASE) {
                    filter { it.gameId == GameType.BASE }
                } else {
                    filter { it.gameId == GameType.ROYAL && basePersonasSet.contains(it.name) || it.gameId == GameType.BASE && !royalSet.contains(it.name) }
                }
            }
            else -> {
                val basePersonasSet = filter { it.gameId == GameType.BASE }.map { it.name }.toSet()
                filter { it.gameId == GameType.ROYAL && !basePersonasSet.contains(it.name) }
            }
        }
    }
}