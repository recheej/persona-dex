package com.persona5dex.viewmodels;

import com.persona5dex.models.Persona;
import com.persona5dex.repositories.PersonaTransferRepository;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaDetailViewModel {

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

    public String getPersonaName(int personaID){
        return this.repository.getPersonaName(personaID);
    }
}
