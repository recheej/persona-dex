package com.persona5dex.dagger.AndroidViewModels;

import android.content.Context;

import com.persona5dex.Persona5Application;
import com.persona5dex.models.RawPersona;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.MainPersonaRoomRepository;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

@Module
public class AndroidViewModelRepositoryModule {

    @Provides
    @ViewModelScope
    MainPersonaRepository mainPersonaRepository(PersonaDatabase database, @Named("applicationContext") Context applicationContext, Lazy<RawPersona[]> rawPersonas){
        return new MainPersonaRoomRepository(database, rawPersonas);
    }
}