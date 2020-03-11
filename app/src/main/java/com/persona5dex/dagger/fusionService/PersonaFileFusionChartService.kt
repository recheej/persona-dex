package com.persona5dex.dagger.fusionService

import android.content.Context
import androidx.annotation.RawRes
import androidx.annotation.WorkerThread
import com.persona5dex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

abstract class PersonaFileFusionChartService(
        private val context: Context
) : FusionChartService {
    @RawRes
    protected abstract fun getFileRes(): Int

    @WorkerThread
    protected abstract suspend fun getFusionChart(fileInputStream: InputStream): FusionChart

    final override suspend fun getFusionChart(): FusionChart =
            withContext(Dispatchers.IO) {
                getFusionChart(context.resources.openRawResource(getFileRes()))
            }
}