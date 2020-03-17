package com.persona5dex.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.GameType;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.CustomPersonaRepository;

import java.util.List;

/**
 * Created by reche on 1/4/2018.
 */

public class PersonaListViewModelFactory implements ViewModelProvider.Factory {

    private final List<MainListPersona> personas;
    private CustomPersonaRepository customPersonaRepository;
    private final ArcanaNameProvider arcanaNameProvider;
    private final GameType gameType;

    public PersonaListViewModelFactory(List<MainListPersona> personas, ArcanaNameProvider arcanaNameProvider, GameType gameType){
        this.personas = personas;
        this.customPersonaRepository = new CustomPersonaRepository(personas);
        this.arcanaNameProvider = arcanaNameProvider;
        this.gameType = gameType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaMainListViewModel(customPersonaRepository, arcanaNameProvider);
        }

        throw new RuntimeException("could not get view model");
    }
}
