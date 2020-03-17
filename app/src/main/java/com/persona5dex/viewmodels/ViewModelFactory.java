package com.persona5dex.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.models.GameType;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
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
    private final Lazy<ArcanaNameProvider> arcanaNameProviderLazy;
    private final Lazy<PersonaFileUtilities> personaFileUtilitiesLazy;
    private final Lazy<GameType> gameTypeLazy;

    @Inject
    public ViewModelFactory(Lazy<PersonaSkillsRepository> skillsRepositoryLazy,
                            Lazy<MainPersonaRepository> mainPersonaRepositoryLazy,
                            Lazy<PersonaDisplayEdgesRepository> edgesRepositoryLazy,
                            @Named("applicationContext") Context applicationContext,
                            Lazy<ArcanaNameProvider> arcanaNameProviderLazy,
                            Lazy<PersonaFileUtilities> personaFileUtilitiesLazy,
                            Lazy<GameType> gameTypeLazy) {
        this.skillsRepositoryLazy = skillsRepositoryLazy;
        this.mainPersonaRepositoryLazy = mainPersonaRepositoryLazy;
        this.edgesRepositoryLazy = edgesRepositoryLazy;
        this.applicationContext = applicationContext;
        this.arcanaNameProviderLazy = arcanaNameProviderLazy;
        this.personaFileUtilitiesLazy = personaFileUtilitiesLazy;
        this.gameTypeLazy = gameTypeLazy;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == PersonaDetailSkillsViewModel.class) {
            return (T) new PersonaDetailSkillsViewModel(skillsRepositoryLazy.get());
        } else if(modelClass == PersonaMainListViewModel.class) {
            return (T) new PersonaMainListViewModel(mainPersonaRepositoryLazy.get(), arcanaNameProviderLazy.get());
        } else if(modelClass == PersonaFusionViewModel.class) {
            return (T) new PersonaFusionViewModel(edgesRepositoryLazy.get(),
                    mainPersonaRepositoryLazy.get());
        } else if(modelClass == AdvancedFusionViewModel.class) {
            return (T) new AdvancedFusionViewModel((Application) applicationContext,
                    mainPersonaRepositoryLazy.get(), personaFileUtilitiesLazy.get());
        } else if(modelClass == SettingsViewModel.class) {
            return (T) new SettingsViewModel(mainPersonaRepositoryLazy.get());
        }

        throw new RuntimeException("could not get view model");
    }
}
