package com.persona5dex.onboarding

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.persona5dex.R
import com.persona5dex.SHARED_PREF_KEY_GAME_TYPE
import com.persona5dex.SHARED_PREF_ONBOARDING_COMPLETE
import com.persona5dex.models.GameType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingViewModel(
        private val defaultSharedPreferences: SharedPreferences,
        private val application: Application
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
        data class NextStepPagerState(val gameType: GameType) : OnboardingPagerState()
        object OnboardingComplete : OnboardingPagerState()
    }
}

class OnboardingViewModelFactory(private val defaultSharedPreferences: SharedPreferences, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            OnboardingViewModel(defaultSharedPreferences, application) as T

}