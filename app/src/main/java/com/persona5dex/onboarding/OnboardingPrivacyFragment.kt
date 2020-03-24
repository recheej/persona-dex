package com.persona5dex.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.persona5dex.R
import com.persona5dex.extensions.toPersonaApplication
import com.persona5dex.fragments.BaseFragment
import javax.inject.Inject
import javax.inject.Named

class OnboardingPrivacyFragment : BaseFragment() {

    @Inject
    @field:Named("defaultSharedPreferences")
    lateinit var defaultSharedPreferences: SharedPreferences

    private lateinit var viewModel: OnboardingViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityComponent.plus().inject(this)

        viewModel = OnboardingViewModelFactory(defaultSharedPreferences, activity.toPersonaApplication()).let {
            ViewModelProvider(requireActivity(), it).get(OnboardingViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.onboarding_privacy, container, false)

        view.findViewById<Button>(R.id.btn_onboarding_done).apply {
            setOnClickListener {
                viewModel.setOnboardingComplete()
            }
        }

        view.findViewById<TextView>(R.id.privacy_text_summary).apply {
            var privacyPrompt = getString(R.string.privacy_prompt)
            privacyPrompt += getString(R.string.privacy_policy_url)

            val privacyPromptSpannable = SpannableString(privacyPrompt)
            Linkify.addLinks(privacyPromptSpannable, Linkify.WEB_URLS)

            text = privacyPromptSpannable
            movementMethod = LinkMovementMethod.getInstance()
        }

        return view
    }

    companion object {
        fun newInstance() = OnboardingPrivacyFragment()
    }
}