package com.persona5dex.uploaddatabase

import android.net.Uri
import androidx.work.Worker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.persona5dex.models.room.PersonaDatabase
import java.io.File

const val DATABASES_PATH = "databases"

class UploadDatabaseWorker: Worker() {
    override fun doWork(): WorkerResult {
        val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageReference = firebaseStorage.reference

        val databaseFile: File = PersonaDatabase.getPersonaDatabase(applicationContext).databaseFile
        val databaseUri = Uri.fromFile(databaseFile)

        val uploadpath = "$DATABASES_PATH/$databaseUri-${System.currentTimeMillis()}"
        val storageRef: StorageReference = storageReference.child(uploadpath)

        val uploadTask: UploadTask = storageRef.putFile(databaseUri)
        uploadTask.addOnFailureListener({
            //how to handle??
        })

        return WorkerResult.SUCCESS
    }
}