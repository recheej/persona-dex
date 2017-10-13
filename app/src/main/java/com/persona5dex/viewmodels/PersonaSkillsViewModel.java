package com.persona5dex.viewmodels;

import com.persona5dex.models.Persona;
import com.persona5dex.models.Persona.Stats;
import com.persona5dex.models.Skill;
import com.persona5dex.repositories.PersonaTransferRepository;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/12/2017.
 */

public class PersonaSkillsViewModel {

    private final PersonaTransferRepository transferRepository;
    private Persona detailPersona;

    @Inject
    public PersonaSkillsViewModel(PersonaTransferRepository repository){
        this.transferRepository = repository;
        this.detailPersona = transferRepository.getDetailPersona();
    }

    public Skill[] getPersonaSkills(){
        Skill[] personaSkills = this.detailPersona.getPersonaSkills();
        Arrays.sort(personaSkills, new Comparator<Skill>() {
            @Override
            public int compare(Skill o1, Skill o2) {
                if(o1.getLevel() < o2.getLevel()){
                    return -1;
                }

                if(o1.getLevel() > o2.getLevel()){
                    return 1;
                }

                //if the levels are equal, compare by name;
                return o1.getName().compareTo(o2.getName());
            }
        });

        return personaSkills;
    }
}