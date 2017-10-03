package com.example.rechee.persona5calculator.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaDetailViewModel extends ViewModel {

    private PersonaTransferRepository repository;

    @Inject
    public PersonaDetailViewModel(PersonaTransferRepository repository){
        this.repository = repository;
    }

    public Persona getDetailPersona(){
        return repository.getDetailPersona();
    }

    public void storePersonaForFusion(Persona personaForFusion){
        repository.storePersonaForFusion(personaForFusion);
    }

    public int getPersonaForFusion(){
        return repository.getPersonaForFusion();
    }
}
