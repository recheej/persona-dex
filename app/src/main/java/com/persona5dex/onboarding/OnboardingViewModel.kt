package com.persona5dex.onboarding

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.persona5dex.SHARED_PREF_KEY_GAME_TYPE
import com.persona5dex.SHARED_PREF_ONBOARDING_COMPLETE
import com.persona5dex.models.GameType

class OnboardingViewModel(
        private val defaultSharedPreferences: SharedPreferences
) : ViewModel() {

    private val gameTypeLiveData = MutableLiveData<GameType>()

    val nextStepState = MediatorLiveData<OnboardingPagerState>().apply {
        addSource(gameTypeLiveData) {
            defaultSharedPreferences.edit()
                    .putInt(SHARED_PREF_KEY_GAME_TYPE, it.value)
                    .apply()
            value = OnboardingPagerState.NextStepPagerState(it)
        }
    }

    fun setGameType(gameType: GameType) {
        gameTypeLiveData.value = gameType
    }

    fun setOnboardingComplete() {
        defaultSharedPreferences.edit()
                .putBoolean(SHARED_PREF_ONBOARDING_COMPLETE, true)
                .apply()
        nextStepState.value = OnboardingPagerState.OnboardingComplete
    }

    sealed class OnboardingPagerState {
        data class NextStepPagerState(val gameType: GameType) : OnboardingPagerState()
        object OnboardingComplete : OnboardingPagerState()
    }
}

class OnboardingViewModelFactory(private val defaultSharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            OnboardingViewModel(defaultSharedPreferences) as T

}