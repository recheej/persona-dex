package com.persona5dex.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.persona5dex.R
import com.persona5dex.extensions.toPersonaApplication
import com.persona5dex.fragments.BaseFragment
import com.persona5dex.jobs.PersonaJobCreator
import com.persona5dex.models.GameType
import javax.inject.Inject
import javax.inject.Named

class OnboardingChooseGameFragment : BaseFragment() {

    @Inject
    @field:Named("defaultSharedPreferences")
    lateinit var defaultSharedPreferences: SharedPreferences

    @Inject
    internal lateinit var personaJobCreator: PersonaJobCreator

    private lateinit var viewModel: OnboardingViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityComponent.inject(this)

        viewModel = OnboardingViewModelFactory(defaultSharedPreferences, activity.toPersonaApplication(), personaJobCreator).let {
            ViewModelProvider(requireActivity(), it).get(OnboardingViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.onboarding_one, container, false)
        view.findViewById<Button>(R.id.skip_onboarding_button).apply {
            setOnClickListener {
                viewModel.setOnboardingComplete()
            }
        }

        view.findViewById<Button>(R.id.base_game_button).apply {
            setOnClickListener {
                viewModel.setGameType(GameType.BASE)
            }
        }

        view.findViewById<Button>(R.id.royal_game_button).apply {
            setOnClickListener {
                viewModel.setGameType(GameType.ROYAL)
            }
        }

        return view
    }

    companion object {
        fun newInstance() = OnboardingChooseGameFragment()
    }
}