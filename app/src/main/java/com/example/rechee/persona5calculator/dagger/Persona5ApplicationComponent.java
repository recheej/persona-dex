package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@Persona5ApplicationScope
@Component(modules = { ApplicationContextModule.class, ApplicationRepositoryModule.class, PersonaFileModule.class})
public interface Persona5ApplicationComponent {
    @Named("applicationContext") Context getContext();
    @Named("applicationGson") Gson gson();
    @Named("personaByName") Persona[] allPersonasByName();
}
