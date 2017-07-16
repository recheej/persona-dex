package com.example.rechee.persona5calculator.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;


import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListViewModel extends ViewModel {

    private final PersonaRepository repository;

    @Inject
    public PersonaListViewModel(PersonaRepository repository){
        this.repository = repository;
    }

    public Persona[] getAllPersonas() {
        return repository.allPersonas();
    }

    public Persona[] filterPersonas(Persona[] personaToFilter, String personaNameQuery){
        return PersonaUtilities.filterPersonaByName(personaToFilter, personaNameQuery);
    }
}
