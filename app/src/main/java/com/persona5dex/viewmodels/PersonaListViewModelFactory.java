package com.persona5dex.viewmodels;

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

    public PersonaListViewModelFactory(ArcanaNameProvider arcanaNameProvider, GameType gameType, PersonaRepository personaRepository) {
        this.arcanaNameProvider = arcanaNameProvider;
        this.gameType = gameType;
        this.personaRepository = personaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaMainListViewModel(arcanaNameProvider, gameType, personaRepository);
        }

        throw new RuntimeException("could not get view model");
    }
}
