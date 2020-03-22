package com.persona5dex.dagger.contentProvider;

import com.persona5dex.searchSuggestions.PersonaNameProvider;

import dagger.Component;

/**
 * Created by Rechee on 7/3/2017.
 */

@ContentProviderScope
@Component(
        modules = {
                ContentProviderContextModule.class
        },
        dependencies = {
        }
)
public interface ContentProviderComponent {
    void inject(PersonaNameProvider provider);
}
