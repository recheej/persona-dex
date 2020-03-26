package com.persona5dex.onboarding

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class OnboardingActivityResultContract(private val context: Context) : ActivityResultContract<Void?, Void?>() {
    override fun createIntent(input: Void?): Intent =
            Intent(context, OnboardingActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): Void? = null
}