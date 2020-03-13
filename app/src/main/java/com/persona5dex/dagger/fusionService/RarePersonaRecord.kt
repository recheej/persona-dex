package com.persona5dex.dagger.fusionService

import com.persona5dex.models.Enumerations

data class RarePersonaRecord(val rarePersonaMap: Map<Enumerations.Arcana, Array<Int>>, val rarePersonaList: Array<String>)