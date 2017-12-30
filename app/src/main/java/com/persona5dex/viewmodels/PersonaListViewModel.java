package com.persona5dex.viewmodels;

import com.persona5dex.PersonaUtilities;
import com.persona5dex.models.Persona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.PersonaRepository;
import com.persona5dex.repositories.PersonaTransferRepository;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListViewModel {

    private final PersonaTransferRepository transferRepository;
    private final Persona[] allPersonas;

    private final Comparator<Persona> sortByPersonaNameDesc;
    private final Comparator<Persona> sortByPersonaNameAsc;
    private final Comparator<Persona> sortByPersonaLevelAsc;
    private final Comparator<Persona> sortByPersonaLevelDesc;
    private final Comparator<Persona> sortByPersonaArcanaAsc;
    private final Comparator<Persona> sortByPersonaArcanaDesc;

    @Inject
    public PersonaListViewModel(PersonaRepository repository, PersonaTransferRepository transferRepository){
        this.allPersonas = repository.allPersonas();
        this.transferRepository = transferRepository;

        sortByPersonaNameAsc = new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return o1.name.compareTo(o2.name);
            }
        };

        sortByPersonaNameDesc = new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return o1.name.compareTo(o2.name) * -1;
            }
        };

        sortByPersonaLevelAsc = new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return Integer.compare(o1.level, o2.level);
            }
        };

        sortByPersonaLevelDesc = new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return Integer.compare(o1.level, o2.level) * -1;
            }
        };

        sortByPersonaArcanaAsc = new Comparator<Persona>() {

            @Override
            public int compare(Persona o1, Persona o2) {
                return o1.arcanaName.compareTo(o2.arcanaName);
            }
        };

        sortByPersonaArcanaDesc = new Comparator<Persona>() {

            @Override
            public int compare(Persona o1, Persona o2) {
                return o1.arcanaName.compareTo(o2.arcanaName) * -1;
            }
        };
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

    public void sortPersonasByName(Persona[] personas, boolean asc){

        if(personas.length == 1){
            return;
        }

        if(asc){
            Arrays.sort(personas, sortByPersonaNameAsc);
        }
        else{
            Arrays.sort(personas, sortByPersonaNameDesc);
        }
    }

    public void sortPersonasByLevel(Persona[] personas, boolean asc){

        if(personas.length == 1){
            return;
        }

        if(asc){
            Arrays.sort(personas, sortByPersonaLevelAsc);
        }
        else{
            Arrays.sort(personas, sortByPersonaLevelDesc);
        }
    }

    public Persona[] filterPersonas(PersonaFilterArgs filterArgs, Persona[] personasToFilter) {
        List<Persona> filteredPersonas = new ArrayList<>(personasToFilter.length);

        String filterArcanaName;
        if(filterArgs.arcanaName == null){
            filterArcanaName = "";
        }
        else{
            filterArcanaName = filterArgs.arcanaName.toLowerCase().replace("_", " ");
        }

        for (Persona persona : personasToFilter) {

            if(persona.rare && !filterArgs.rarePersona){
                continue;
            }

            if(persona.dlc && !filterArgs.dlcPersona){
                continue;
            }

            if(filterArgs.arcanaName != null && !filterArgs.arcanaName.isEmpty()){
                String personaArcanaName;

                if(persona.arcanaName == null){
                    personaArcanaName = persona.arcana.getName();
                }
                else{
                    personaArcanaName = persona.arcanaName;
                }

                personaArcanaName = personaArcanaName.toLowerCase().replace("_", " ");
                if(!personaArcanaName.equals(filterArcanaName)){
                    continue;
                }
            }

            if(persona.level < filterArgs.minLevel || persona.level > filterArgs.maxLevel){
                continue;
            }

            filteredPersonas.add(persona);
        }

        return filteredPersonas.toArray(new Persona[filteredPersonas.size()]);
    }

    public void sortPersonasByArcana(Persona[] personas, boolean asc) {
        if(personas.length == 1){
            return;
        }

        if(asc){
            Arrays.sort(personas, sortByPersonaArcanaAsc);
        }
        else{
            Arrays.sort(personas, sortByPersonaArcanaDesc);
        }
    }
}
