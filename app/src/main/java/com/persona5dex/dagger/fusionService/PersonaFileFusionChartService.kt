package com.persona5dex.dagger.fusionService

import android.content.Context
import androidx.annotation.RawRes
import androidx.annotation.WorkerThread
import com.persona5dex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

abstract class PersonaFileFusionChartService(
        context: Context
) : FusionChartService, PersonaFileService<FusionChart>(context) {

    final override suspend fun getFusionChart(): FusionChart = parseFile()
}