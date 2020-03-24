package com.persona5dex.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.lifecycle.ViewModelProvider
import com.persona5dex.R
import com.persona5dex.extensions.toPersonaApplication
import com.persona5dex.fragments.BaseFragment
import com.persona5dex.jobs.PersonaJobCreator
import javax.inject.Inject
import javax.inject.Named

class OnboardingThemeChooserFragment : BaseFragment() {

    @Inject
    @field:Named("defaultSharedPreferences")
    lateinit var defaultSharedPreferences: SharedPreferences

    @Inject
    internal lateinit var personaJobCreator: PersonaJobCreator

    private lateinit var viewModel: OnboardingViewModel
    private lateinit var radioGroup: RadioGroup

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityComponent.plus().inject(this)

        viewModel = OnboardingViewModelFactory(defaultSharedPreferences, activity.toPersonaApplication(), personaJobCreator).let {
            ViewModelProvider(requireActivity(), it).get(OnboardingViewModel::class.java)
        }

        radioGroup.setOnCheckedChangeListener { _: RadioGroup, idRes: Int ->
            val nightModeForRadioSelection = getNightModeForRadioSelection(idRes)
            setDefaultNightMode(nightModeForRadioSelection)
            viewModel.setNightMode(nightModeForRadioSelection)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.onboarding_theme, container, false)
        radioGroup = view.findViewById<RadioGroup>(R.id.theme_radio_group)
        radioGroup.check(R.id.radio_system_default)

        view.findViewById<Button>(R.id.btn_onboarding_next).let {
            it.setOnClickListener {
                viewModel.incrementNextStep()
            }
        }

        return view
    }

    private fun getNightModeForRadioSelection(idRes: Int) =
            when (idRes) {
                R.id.radio_system_default -> MODE_NIGHT_FOLLOW_SYSTEM
                R.id.radio_light -> MODE_NIGHT_NO
                R.id.radio_dark -> MODE_NIGHT_YES
                else -> error("cannot find mode night for idRes: $idRes")
            }

    companion object {
        fun newInstance() = OnboardingThemeChooserFragment()
    }
}
