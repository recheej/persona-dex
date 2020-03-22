package com.persona5dex.dagger.application;

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

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidViewModelRepositoryModule {

    @Provides
    MainPersonaRepository mainPersonaRepository(PersonaDatabase database){
        return new MainPersonaRoomRepository(database);
    }

    @Provides
    PersonaDetailRepository personaDetailRepository(PersonaDatabase database){
        return new PersonaDetailRoomRepository(database.personaDao());
    }

    @Provides
    PersonaElementsRepository elementsRepository(PersonaDatabase database){
        return new PersonaElementsRoomRepository(database.personaDao());
    }

    @Provides
    PersonaSkillsRepository skillsRepository(PersonaDatabase database){
        return new PersonaSkillsRoomRepository(database.skillDao());
    }

    @Provides
    PersonaDisplayEdgesRepository edgesRepository(PersonaDatabase database){
        return new PersonaDisplayEdgesRoomRepository(database.personaDao());
    }
}