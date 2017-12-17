package com.persona5dex.dagger.viewModels;

import android.content.Context;

import com.persona5dex.models.RawPersona;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.MainPersonaRoomRepository;
import com.persona5dex.repositories.PersonaDetailRepository;
import com.persona5dex.repositories.PersonaDetailRoomRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRoomRepository;
import com.persona5dex.repositories.PersonaElementsRepository;
import com.persona5dex.repositories.PersonaElementsRoomRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;
import com.persona5dex.repositories.PersonaSkillsRoomRepository;

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

    @Provides
    @ViewModelScope
    PersonaSkillsRepository skillsRepository(PersonaDatabase database){
        return new PersonaSkillsRoomRepository(database.personaDao());
    }

    @Provides
    @ViewModelScope
    PersonaDisplayEdgesRepository edgesRepository(PersonaDatabase database){
        return new PersonaDisplayEdgesRoomRepository(database.personaDao());
    }
}