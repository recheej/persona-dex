package com.persona5dex.jobs

import androidx.lifecycle.Transformations
import androidx.work.*
import com.persona5dex.fusionService.GenerateFusionWorker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonaJobCreator @Inject constructor(
        private val workManager: WorkManager
) {
    fun scheduleUniqueSingleJob(jobName: String) {
        workManager.enqueueUniqueWork(jobName, ExistingWorkPolicy.REPLACE, getRequest(jobName))
    }

    fun scheduleGenerateFusionJob() =
            scheduleUniqueSingleJob(
                    JOB_NAME_GENERATE_FUSION
            )

    fun getStateForGenerateFusionJob() =
            getStateForUniqueJob(JOB_NAME_GENERATE_FUSION)

    fun getStateForUniqueJob(jobName: String) =
            Transformations.map(workManager.getWorkInfosForUniqueWorkLiveData(jobName)) { workInfos ->
                workInfos.maxBy { it.id }?.state ?: WorkInfo.State.SUCCEEDED
            }

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
    }
}