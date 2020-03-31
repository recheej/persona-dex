package com.persona5dex.models

enum class GameType(val value: Int) {
    BASE(1), ROYAL(2);

    companion object {
        @JvmStatic
        @JvmName("getGameType")
        fun Int.toGameType() =
                values().first { it.value == this }
    }
}