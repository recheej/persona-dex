package com.persona5dex.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;

/**
 * Created by reche on 1/4/2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Lazy<PersonaSkillsRepository> skillsRepositoryLazy;
    private final Lazy<MainPersonaRepository> mainPersonaRepositoryLazy;
    private final Lazy<PersonaDisplayEdgesRepository> edgesRepositoryLazy;
    private final Context applicationContext;

    @Inject
    public ViewModelFactory(Lazy<PersonaSkillsRepository> skillsRepositoryLazy,
                            Lazy<MainPersonaRepository> mainPersonaRepositoryLazy,
                            Lazy<PersonaDisplayEdgesRepository> edgesRepositoryLazy,
                            @Named("applicationContext") Context applicationContext){
        this.skillsRepositoryLazy = skillsRepositoryLazy;
        this.mainPersonaRepositoryLazy = mainPersonaRepositoryLazy;
        this.edgesRepositoryLazy = edgesRepositoryLazy;
        this.applicationContext = applicationContext;
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
            return (T) new PersonaFusionViewModel(edgesRepositoryLazy.get(),
                    mainPersonaRepositoryLazy.get());
        }
        else if(modelClass == AdvancedFusionViewModel.class){
            return  (T) new AdvancedFusionViewModel((Application) applicationContext,
                    mainPersonaRepositoryLazy.get());
        }

        throw new RuntimeException("could not get view model");
    }
}
