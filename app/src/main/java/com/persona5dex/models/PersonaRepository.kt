package com.persona5dex.models

interface PersonaRepository {
    suspend fun getPersonas(): List<MainListPersona>
}