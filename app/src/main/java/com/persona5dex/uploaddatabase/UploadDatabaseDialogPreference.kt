package com.persona5dex.uploaddatabase

import android.content.Context
import android.support.v7.preference.DialogPreference
import com.persona5dex.R

class UploadDatabaseDialogPreference(context: Context?) : DialogPreference(context) {
    init {
        dialogLayoutResource = R.layout.upload_db_dialog
        key = "upload-db"
        if (context != null) {
            positiveButtonText = context.getString(android.R.string.ok)
            negativeButtonText = context.getString(android.R.string.cancel)
            title = context.getString(R.string.upload_db)
            summary = context.getString(R.string.summary_upload_db)
        }

        dialogIcon = null
    }
}