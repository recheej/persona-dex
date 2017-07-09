package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.PersonaNameProvider;
import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepository;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 7/3/2017.
 */

@PersonaNameProviderScope
@Component(modules = {PersonaNameProviderDataModule.class, PersonaNameProviderContextModule.class, PersonaNameProviderFileModule.class})
public interface PersonaNameProviderComponent {
    void inject(PersonaNameProvider provider);
    @Named("personaNameProviderContext") Context context();
    PersonaSuggestionRepository suggestionRepository();
}
