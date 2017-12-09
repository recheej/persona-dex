package com.persona5dex.dagger.AndroidViewModels;

import android.content.Context;

import com.persona5dex.Persona5Application;
import com.persona5dex.models.RawPersona;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.MainPersonaRoomRepository;
import com.persona5dex.repositories.PersonaDetailRepository;
import com.persona5dex.repositories.PersonaDetailRoomRepository;
import com.persona5dex.repositories.PersonaElementsRepository;
import com.persona5dex.repositories.PersonaElementsRoomRepository;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

@Module
public class AndroidViewModelRepositoryModule {

    @Provides
    @ViewModelScope
    MainPersonaRepository mainPersonaRepository(PersonaDatabase database, @Named("applicationContext") Context applicationContext, Lazy<RawPersona[]> rawPersonas){
        return new MainPersonaRoomRepository(database, rawPersonas, applicationContext);
    }

    @Provides
    @ViewModelScope
    PersonaDetailRepository personaDetailRepository(PersonaDatabase database){
        return new PersonaDetailRoomRepository(database.personaDao());
    }

    @Provides
    @ViewModelScope
    PersonaElementsRepository elementsRepository(PersonaDatabase database){
        return new PersonaElementsRoomRepository(database.personaDao());
    }
}