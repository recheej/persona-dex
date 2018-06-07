package com.persona5dex.uploaddatabase

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.preference.DialogPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver

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
        val okButtonClicked = which == -1
        if(okButtonClicked) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

            val myJob = dispatcher.newJobBuilder()
                    .setService(UploadDatabaseJobService::class.java) // the JobService that will be called
                    .setTag(TAG)        // uniquely identifies the job
                    .build()

            dispatcher.mustSchedule(myJob)
        }
    }
}