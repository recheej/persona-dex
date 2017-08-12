package com.example.rechee.persona5calculator.viewmodels;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.Persona.Stats;
import com.example.rechee.persona5calculator.models.Skill;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;

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
