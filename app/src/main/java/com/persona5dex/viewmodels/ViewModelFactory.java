package com.persona5dex.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.persona5dex.repositories.PersonaSkillsRepository;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by reche on 1/4/2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Lazy<PersonaSkillsRepository> skillsRepositoryLazy;

    @Inject
    public ViewModelFactory(Lazy<PersonaSkillsRepository> skillsRepositoryLazy){
        this.skillsRepositoryLazy = skillsRepositoryLazy;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaDetailSkillsViewModel.class){
            return (T) new PersonaDetailSkillsViewModel(skillsRepositoryLazy.get());
        }

        throw new RuntimeException("could not get view model");
    }
}
