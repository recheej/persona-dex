package com.persona5dex.dagger;

import com.persona5dex.PersonaNameProvider;

import dagger.Component;

/**
 * Created by Rechee on 7/3/2017.
 */

@PersonaNameProviderScope
@Component(
        modules = {
                NameProviderRepositoryModule.class
        },
        dependencies = {
        }
)
public interface PersonaNameProviderComponent {
    void inject(PersonaNameProvider provider);
}
