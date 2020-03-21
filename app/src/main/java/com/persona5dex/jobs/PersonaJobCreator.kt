package com.persona5dex.jobs

import androidx.work.*
import com.persona5dex.dagger.application.ApplicationScope
import com.persona5dex.fusionService.GenerateFusionWorker
import com.persona5dex.models.GameType
import javax.inject.Inject

@ApplicationScope
class PersonaJobCreator @Inject constructor(
        private val workManager: WorkManager
) {
    fun scheduleUniqueSingleJob(jobName: String, data: Data) {
        workManager.enqueueUniqueWork(jobName, ExistingWorkPolicy.REPLACE, getRequest(jobName))
    }

    fun scheduleGenerateFusionJob(gameType: GameType) =
            scheduleUniqueSingleJob(
                    JOB_NAME_GENERATE_FUSION,
                    Data.Builder().putInt(JOB_PARAM_GAME_TYPE, gameType.value).build()
            )

    private fun getRequest(jobName: String): OneTimeWorkRequest {
        return when (jobName) {
            JOB_NAME_GENERATE_FUSION -> {
                getConstraints(jobName).let {
                    OneTimeWorkRequest.Builder(GenerateFusionWorker::class.java)
                            .build()
                }
            }
            else -> error("cannot get request for job with name: $jobName")
        }
    }

    private fun getConstraints(jobName: String) =
            when (jobName) {
                JOB_NAME_GENERATE_FUSION -> Constraints.Builder()
                        .setRequiresStorageNotLow(true)
                        .build()
                else -> error("cannot get constraints for job with name: $jobName")
            }


    companion object {
        const val JOB_NAME_GENERATE_FUSION = "JOB_GENERATE_FUSION"

        const val JOB_PARAM_GAME_TYPE = "gameType"
    }
}