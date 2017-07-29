package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.PersonaNameProvider;

import dagger.Component;

/**
 * Created by Rechee on 7/3/2017.
 */

@PersonaNameProviderScope
@Component(
        modules = {
                NameProviderRepositoryModule.class,
                PersonaFileModule.class
        },
        dependencies = {
        }
)
public interface PersonaNameProviderComponent {
    void inject(PersonaNameProvider provider);
}
