package com.persona5dex.uploaddatabase

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.preference.DialogPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.widget.Toast
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.persona5dex.BuildConfig
import com.persona5dex.R
import com.persona5dex.uploaddatabase.UploadDatabaseJobService.Companion.JOB_TAG
import java.util.concurrent.TimeUnit

const val UPLOAD_FILE_PREF_KEY = "${BuildConfig.APPLICATION_ID}.UPLOAD_PREF"
const val LAST_UPLOAD_TIME_KEY = "last_upload_time"

class UploadDbPreferenceDialogFragment: PreferenceDialogFragmentCompat() {

    companion object {
        fun newInstance(preference: Preference): UploadDbPreferenceDialogFragment {
            val fragment = UploadDbPreferenceDialogFragment()
            val bundle = Bundle(1)
            bundle.putString("key", preference.key)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
    }

    override fun getPreference(): DialogPreference {
        return UploadDatabaseDialogPreference(context)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val uploadSharedPreferences = activity?.applicationContext?.
                getSharedPreferences(UPLOAD_FILE_PREF_KEY, Context.MODE_PRIVATE)

        fun uploadingTooQuickly(): Boolean{
            val twoMinutesAgo = TimeUnit.MINUTES.toMillis(2)
            val lastUploadTime = uploadSharedPreferences?.getLong(LAST_UPLOAD_TIME_KEY, twoMinutesAgo)
                    ?: twoMinutesAgo

            return (System.currentTimeMillis() - lastUploadTime) < twoMinutesAgo
        }

        val okButtonClicked = which == -1
        if(okButtonClicked) {
            if(uploadingTooQuickly()){
                Toast.makeText(this.context, R.string.upload_too_soon_prompt, Toast.LENGTH_LONG).show()
            }
            else{
                val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

                val myJob = dispatcher.newJobBuilder()
                        .setService(UploadDatabaseJobService::class.java) // the JobService that will be called
                        .setTag(JOB_TAG)        // uniquely identifies the job
                        .build()

                dispatcher.mustSchedule(myJob)
                Toast.makeText(this.context, R.string.upload_sent, Toast.LENGTH_SHORT).show()

                uploadSharedPreferences?.edit()?.
                        putLong(LAST_UPLOAD_TIME_KEY, System.currentTimeMillis())?.apply()
            }
        }

    }
}