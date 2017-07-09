package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 7/3/2017.
 */


@ViewModelScope
@Component(modules = {RepositoryModule.class, PersonaFileModule.class}, dependencies = {Persona5ApplicationComponent.class})
public interface ViewModelComponent {
    PersonaRepository personaRepository();
    @Named("personaFileContents") String personaFileContents();
}
