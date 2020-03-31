package com.persona5dex.viewmodels;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.GameType;
import com.persona5dex.models.PersonaRepository;

/**
 * Created by reche on 1/4/2018.
 */

public class PersonaListViewModelFactory implements ViewModelProvider.Factory {

    private final ArcanaNameProvider arcanaNameProvider;
    private final GameType gameType;
    private final PersonaRepository personaRepository;
    private final SharedPreferences defaultSharedPreferences;

    public PersonaListViewModelFactory(ArcanaNameProvider arcanaNameProvider, GameType gameType, PersonaRepository personaRepository, SharedPreferences defaultSharedPreferences) {
        this.arcanaNameProvider = arcanaNameProvider;
        this.gameType = gameType;
        this.personaRepository = personaRepository;
        this.defaultSharedPreferences = defaultSharedPreferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaMainListViewModel(arcanaNameProvider, gameType, personaRepository, defaultSharedPreferences);
        }

        throw new RuntimeException("could not get view model");
    }
}
