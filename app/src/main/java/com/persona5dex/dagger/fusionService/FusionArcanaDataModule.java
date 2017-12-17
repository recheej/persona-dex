package com.persona5dex.dagger.fusionService;

import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.room.PersonaDatabase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class FusionArcanaDataModule {

    @FusionServiceScope
    @Provides
    @Named("personaByLevel")
    PersonaForFusionService[] personaByLevel(PersonaDatabase database) {
        return database.personaDao().getPersonasByLevel();
    }
}
