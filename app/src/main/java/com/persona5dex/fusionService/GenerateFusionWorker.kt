package com.persona5dex.fusionService

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.persona5dex.extensions.toPersonaApplication
import com.persona5dex.models.room.PersonaFusion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.yield
import javax.inject.Inject

class GenerateFusionWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val application = context.toPersonaApplication()
    private val database = application.database
    private val personaDao = database.personaDao()

    @Inject
    internal lateinit var graphGenerator: PersonaFusionGraphGenerator

    override suspend fun doWork(): Result {
        application.component.inject(this)

        try {
            val personaFusions = graphGenerator.getAllFusions().map {
                PersonaFusion().apply {
                    personaOneID = it.personaOne.id
                    personaTwoID = it.personaTwo.id
                    personaResultID = it.resultPersona.id
                }
            }
            yield()
            personaDao.deleteAndInsertNewFusions(personaFusions)
        } catch (e: CancellationException) {
            Log.e(TAG, "GenerateFusionWorker job cancelled", e)
        }

        return Result.success()
    }

    companion object {
        private const val TAG = "GenerateFusionWorker"
    }
}