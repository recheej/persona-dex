package com.persona5dex.uploaddatabase

import android.content.Context
import android.net.Uri
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.persona5dex.BuildConfig
import com.persona5dex.models.room.PersonaDatabase
import java.io.File

private const val DATABASES_PATH = "databases"



class UploadDatabaseJobService: JobService() {
    companion object {
        const val JOB_TAG = "job-service-upload-db"
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(job: JobParameters?): Boolean {

        Thread(Runnable {
            val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageReference = firebaseStorage.reference

            val databaseFile: File = PersonaDatabase.getPersonaDatabase(applicationContext).databaseFile
            val databaseUri = Uri.fromFile(databaseFile)

            val uploadPath = "$DATABASES_PATH/${FirebaseInstanceId.getInstance().id}-${System.currentTimeMillis()}.db"
            val storageRef: StorageReference = storageReference.child(uploadPath)

            val uploadTask: UploadTask = storageRef.putFile(databaseUri)
            uploadTask.addOnSuccessListener {
                jobFinished(job!!, false)
            }

            uploadTask.addOnFailureListener({
                jobFinished(job!!, true)
            })
        }).start()

        return true
    }
}