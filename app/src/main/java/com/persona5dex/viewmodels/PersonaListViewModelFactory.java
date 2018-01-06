package com.persona5dex.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by reche on 1/4/2018.
 */

public class PersonaListViewModelFactory implements ViewModelProvider.Factory {

    private final List<Integer> personaIDs;

    @Inject
    public PersonaListViewModelFactory(List<MainListPersona> personaIDs){
        this.personaIDs = personaIDs;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaDetailSkillsViewModel(skillsRepositoryLazy.get());
        }

        throw new RuntimeException("could not get view model");
    }
}
