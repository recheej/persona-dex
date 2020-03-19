package com.persona5dex.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.GameType;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.CustomPersonaRepository;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.List;

/**
 * Created by reche on 1/4/2018.
 */

public class PersonaListViewModelFactory implements ViewModelProvider.Factory {

    private final MainPersonaRepository mainPersonaRepository;
    private final ArcanaNameProvider arcanaNameProvider;
    private final GameType gameType;

    public PersonaListViewModelFactory(MainPersonaRepository mainPersonaRepository, ArcanaNameProvider arcanaNameProvider, GameType gameType){
        this.mainPersonaRepository = mainPersonaRepository;
        this.arcanaNameProvider = arcanaNameProvider;
        this.gameType = gameType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaMainListViewModel(arcanaNameProvider, gameType);
        }

        throw new RuntimeException("could not get view model");
    }
}
