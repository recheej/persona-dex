package com.persona5dex.dagger;

import com.persona5dex.models.Persona;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.repositories.PersonaRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
