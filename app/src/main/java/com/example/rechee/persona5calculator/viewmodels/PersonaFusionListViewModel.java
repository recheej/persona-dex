package com.example.rechee.persona5calculator.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListViewModel extends ViewModel {

    private final PersonaEdgesRepository personaEdgeRepository;
    private final PersonaListViewModel personaListViewModel;

    public PersonaFusionListViewModel(PersonaEdgesRepository personaEdgeRepository, PersonaListViewModel personaListViewModel){
        this.personaEdgeRepository = personaEdgeRepository;
        this.personaListViewModel = personaListViewModel;
    }

    public PersonaStore getEdgesForPersona(Persona persona) {
        return personaEdgeRepository.getEdgesForPersona(persona);
    }

    public PersonaStore getEdgesForPersona(String personaName) {
        return personaEdgeRepository.getEdgesForPersona(personaName);
    }

    public void storePersonaForDetail(String personaName){
        personaListViewModel.storePersonaForDetail(personaName);
    }
}
