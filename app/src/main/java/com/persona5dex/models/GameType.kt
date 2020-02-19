package com.persona5dex.models

enum class GameType(val value: Int) {
    BASE(1), ROYAL(2);

    companion object {
        @JvmStatic
        fun getGameType(value: Int) =
                values().first { it.value == value }
    }
}