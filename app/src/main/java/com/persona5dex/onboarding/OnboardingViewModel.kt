package com.persona5dex.onboarding

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.persona5dex.R
import com.persona5dex.SHARED_PREF_KEY_GAME_TYPE
import com.persona5dex.SHARED_PREF_ONBOARDING_COMPLETE
import com.persona5dex.jobs.PersonaJobCreator
import com.persona5dex.models.GameType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val defaultSharedPreferences: SharedPreferences,
    private val application: Application,
    private val personaJobCreator: PersonaJobCreator
) : ViewModel() {

    private val nextStepState = MutableLiveData<OnboardingPagerState>()

    fun getNextStepState(): LiveData<OnboardingPagerState> = nextStepState

    fun incrementNextStep() {
        nextStepState.postValue(OnboardingPagerState.NextStepPagerState)
    }

    @SuppressLint("ApplySharedPref")
    fun setGameType(gameType: GameType) {
        viewModelScope.launch(Dispatchers.IO) {
            defaultSharedPreferences.edit()
                .putInt(SHARED_PREF_KEY_GAME_TYPE, gameType.value)
                .commit()
            incrementNextStep()
        }
    }

    @SuppressLint("ApplySharedPref")
    fun setOnboardingComplete() {
        viewModelScope.launch(Dispatchers.IO) {
            defaultSharedPreferences.edit()
                .putBoolean(SHARED_PREF_ONBOARDING_COMPLETE, true)
                .commit()
            nextStepState.postValue(OnboardingPagerState.OnboardingComplete)
        }
    }

    fun setNightMode(nightMode: Int) {
        defaultSharedPreferences.edit()
            .putString(application.getString(R.string.pref_key_theme), nightMode.toString())
            .apply()
    }

    sealed class OnboardingPagerState {
        object NextStepPagerState : OnboardingPagerState()
        object OnboardingComplete : OnboardingPagerState()
    }
}

class OnboardingViewModelFactory(
    private val defaultSharedPreferences: SharedPreferences,
    private val application: Application,
    val personaJobCreator: PersonaJobCreator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        OnboardingViewModel(defaultSharedPreferences, application, personaJobCreator) as T
}