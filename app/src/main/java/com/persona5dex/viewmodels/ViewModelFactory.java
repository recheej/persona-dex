package com.persona5dex.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.GameType;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

/**
 * Created by reche on 1/4/2018.
 */

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Lazy<PersonaSkillsRepository> skillsRepositoryLazy;
    private final Lazy<MainPersonaRepository> mainPersonaRepositoryLazy;
    private final Lazy<ArcanaNameProvider> arcanaNameProviderLazy;
    private final Lazy<GameType> gameTypeLazy;

    @Inject
    public ViewModelFactory(Lazy<PersonaSkillsRepository> skillsRepositoryLazy,
                            Lazy<MainPersonaRepository> mainPersonaRepositoryLazy,
                            Lazy<ArcanaNameProvider> arcanaNameProviderLazy,
                            Lazy<GameType> gameTypeLazy) {
        this.skillsRepositoryLazy = skillsRepositoryLazy;
        this.mainPersonaRepositoryLazy = mainPersonaRepositoryLazy;
        this.arcanaNameProviderLazy = arcanaNameProviderLazy;
        this.gameTypeLazy = gameTypeLazy;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaDetailSkillsViewModel.class) {
            return (T) new PersonaDetailSkillsViewModel(skillsRepositoryLazy.get());
        } else if(modelClass == PersonaMainListViewModel.class) {
            return (T) new PersonaMainListViewModel(arcanaNameProviderLazy.get(), gameTypeLazy.get());
        } else if(modelClass == SettingsViewModel.class) {
            return (T) new SettingsViewModel(mainPersonaRepositoryLazy.get());
        }

        throw new IllegalStateException("could not get view model for class: " + modelClass);
    }
}
