package com.example.rechee.persona5calculator.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;


import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListViewModel extends ViewModel {

    private final PersonaRepository repository;
    private final PersonaTransferRepository transferRepository;
    private final Persona[] allPersonas;

    @Inject
    public PersonaListViewModel(PersonaRepository repository, PersonaTransferRepository transferRepository){
        this.repository = repository;
        this.allPersonas = repository.allPersonas();
        this.transferRepository = transferRepository;
    }

    public Persona[] getAllPersonas() {
        return this.allPersonas;
    }

    public Persona[] filterPersonas(Persona[] personasToFilter, String personaNameQuery){
        return PersonaUtilities.filterPersonaByName(personasToFilter, personaNameQuery);
    }

    public void storePersonaForDetail(String personaName){
        Persona foundPersona = this.filterPersonas(this.allPersonas, personaName)[0];
        this.transferRepository.storePersonaForDetail(foundPersona);
    }

    public void storePersonaForDetail(Persona personaToStore){
        this.transferRepository.storePersonaForDetail(personaToStore);
    }
}
