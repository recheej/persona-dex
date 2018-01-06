package com.persona5dex.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by reche on 1/4/2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Lazy<PersonaSkillsRepository> skillsRepositoryLazy;
    private final Lazy<MainPersonaRepository> mainPersonaRepositoryLazy;
    private final Lazy<PersonaDisplayEdgesRepository> edgesRepositoryLazy;

    @Inject
    public ViewModelFactory(Lazy<PersonaSkillsRepository> skillsRepositoryLazy,
                            Lazy<MainPersonaRepository> mainPersonaRepositoryLazy,
                            Lazy<PersonaDisplayEdgesRepository> edgesRepositoryLazy){
        this.skillsRepositoryLazy = skillsRepositoryLazy;
        this.mainPersonaRepositoryLazy = mainPersonaRepositoryLazy;
        this.edgesRepositoryLazy = edgesRepositoryLazy;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaDetailSkillsViewModel.class){
            return (T) new PersonaDetailSkillsViewModel(skillsRepositoryLazy.get());
        }
        else if(modelClass == PersonaMainListViewModel.class){
            return (T) new PersonaMainListViewModel(mainPersonaRepositoryLazy.get());
        }
        else if(modelClass == PersonaFusionViewModel.class){
            return (T) new PersonaFusionViewModel(edgesRepositoryLazy.get());
        }

        throw new RuntimeException("could not get view model");
    }
}
