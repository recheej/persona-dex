package com.persona5dex.fragments

enum class PersonaListRepositoryType(val value: Int) {
    PERSONA(0), SKILLS(1);

    companion object {
        @JvmStatic
        fun getRepositoryType(value: Int) =
                values().first { it.value == value }
    }
}