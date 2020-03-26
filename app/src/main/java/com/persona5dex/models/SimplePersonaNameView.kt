package com.persona5dex.models

import androidx.room.DatabaseView

@DatabaseView("""
    select id, name from personas
""")
data class SimplePersonaNameView(val id: Int, val name: String)