package com.persona5dex.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.CustomPersonaRepository;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by reche on 1/4/2018.
 */

public class PersonaListViewModelFactory implements ViewModelProvider.Factory {

    private final List<MainListPersona> personas;
    private CustomPersonaRepository customPersonaRepository;

    public PersonaListViewModelFactory(List<MainListPersona> personas){
        this.personas = personas;
        this.customPersonaRepository = new CustomPersonaRepository(personas);
    }

    public PersonaListViewModelFactory(){
        this.personas = new ArrayList<>(250);
        this.customPersonaRepository = new CustomPersonaRepository(personas);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaMainListViewModel(customPersonaRepository);
        }

        throw new RuntimeException("could not get view model");
    }
}
