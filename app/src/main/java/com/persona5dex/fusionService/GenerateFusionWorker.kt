package com.persona5dex.fusionService

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.persona5dex.extensions.toPersonaApplication
import com.persona5dex.models.room.PersonaFusion
import kotlinx.coroutines.*
import javax.inject.Inject

class GenerateFusionWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val application = context.toPersonaApplication()
    private val database = application.database
    private val personaDao = database.personaDao()

    @Inject
    internal lateinit var graphGenerator: PersonaFusionGraphGenerator

    override suspend fun doWork(): Result {
        application.component.inject(this)

        val personaFusions = graphGenerator.getAllFusions().map {
            PersonaFusion().apply {
                personaOneID = it.personaOne.id
                personaTwoID = it.personaTwo.id
                personaResultID = it.resultPersona.id
            }
        }

        yield()
        personaDao.apply {
            /*
                Received error saying that db is getting truncated so splitting list in half.

                2020-03-30 23:53:38.148 5023-5132/com.persona5dex.debug I/SQLiteConnection: /data/user/0/com.persona5dex.debug/databases/persona-db.db-wal 1231912 bytes: Bigger than 1048576; truncating
            */

            try {
                deleteAllFromPersonaFusions()

                coroutineScope {
                    personaFusions.chunked(personaFusions.size / 4).map {
                        async {
                            insertPersonaFusions(it)
                        }
                    }
                }.awaitAll()
            } catch (ex: Exception) {
                withContext(NonCancellable) {
                    deleteAllFromPersonaFusions()
                    throw ex
                }
            }
        }

        return Result.success()
    }

    companion object {
        private const val TAG = "GenerateFusionWorker"
    }
}