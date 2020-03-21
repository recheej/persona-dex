@file:JvmName("WorkInfoStateUtils")

package com.persona5dex.extensions

import androidx.work.WorkInfo

fun WorkInfo.State.isInProgress() =
        when (this) {
            WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> true
            else -> false
        }

fun WorkInfo.State.isFinished() =
        when (this) {
            WorkInfo.State.SUCCEEDED, WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> true
            else -> false
        }

fun WorkInfo.State.isFinishedSuccessfully() =
        when (this) {
            WorkInfo.State.SUCCEEDED -> true
            else -> false
        }

fun WorkInfo.State.isFinishedWithFailure() =
        when (this) {
            WorkInfo.State.FAILED -> true
            else -> false
        }