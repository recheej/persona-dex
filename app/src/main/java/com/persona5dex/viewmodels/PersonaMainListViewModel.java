package com.persona5dex.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaMainListViewModel extends ViewModel{
    private MediatorLiveData<List<MainListPersona>> filteredPersonas;
    private MutableLiveData<List<MainListPersona>> allPersonas;
    private MutableLiveData<String> personaSearchName;
    private MutableLiveData<Boolean> personasByName;
    private MutableLiveData<Boolean> personasByLevel;
    private MutableLiveData<Boolean> personasByArcana;
    private MutableLiveData<PersonaFilterArgs> personaFilterArgs;

    private MainPersonaRepository repository;

    private Comparator<MainListPersona> sortByPersonaNameDesc;
    private Comparator<MainListPersona> sortByPersonaNameAsc;
    private Comparator<MainListPersona> sortByPersonaLevelAsc;
    private Comparator<MainListPersona> sortByPersonaLevelDesc;
    private Comparator<MainListPersona> sortByPersonaArcanaAsc;
    private Comparator<MainListPersona> sortByPersonaArcanaDesc;

    public PersonaMainListViewModel(final MainPersonaRepository repository){
        setUpViewModel(repository);
    }

    private void setUpViewModel(MainPersonaRepository repository) {
        allPersonas = new MutableLiveData<>();
        filteredPersonas = new MediatorLiveData<>();

        personaSearchName = new MutableLiveData<>();
        personasByName = new MutableLiveData<>();
        personasByLevel = new MutableLiveData<>();
        personasByArcana = new MutableLiveData<>();
        personaFilterArgs = new MutableLiveData<>();

        sortByPersonaNameAsc = new Comparator<MainListPersona>() {
            @Override
            public int compare(MainListPersona o1, MainListPersona o2) {
                return o1.name.compareTo(o2.name);
            }
        };

        sortByPersonaNameDesc = new Comparator<MainListPersona>() {
            @Override
            public int compare(MainListPersona o1, MainListPersona o2) {
                return o1.name.compareTo(o2.name) * -1;
            }
        };

        sortByPersonaLevelAsc = new Comparator<MainListPersona>() {
            @Override
            public int compare(MainListPersona o1, MainListPersona o2) {
                return Integer.compare(o1.level, o2.level);
            }
        };

        sortByPersonaLevelDesc = new Comparator<MainListPersona>() {
            @Override
            public int compare(MainListPersona o1, MainListPersona o2) {
                return Integer.compare(o1.level, o2.level) * -1;
            }
        };

        sortByPersonaArcanaAsc = new Comparator<MainListPersona>() {
            @Override
            public int compare(MainListPersona o1, MainListPersona o2) {
                return o1.arcanaName.compareTo(o2.arcanaName);
            }
        };

        sortByPersonaArcanaDesc = new Comparator<MainListPersona>() {

            @Override
            public int compare(MainListPersona o1, MainListPersona o2) {
                return o1.arcanaName.compareTo(o2.arcanaName) * -1;
            }
        };

        filteredPersonas.addSource(personaSearchName, personaName -> {
            allPersonas.observeForever(personas -> {

                if(personas == null){
                    personas = new ArrayList<>();
                }

                if(personaName == null || personaName.isEmpty()){
                    filteredPersonas.setValue(personas);
                }
                else{
                    List<MainListPersona> finalList = new ArrayList<>();

                    for (MainListPersona mainListPersona : personas) {
                        if(mainListPersona.name.toLowerCase().contains(personaName.toLowerCase())){
                            finalList.add(mainListPersona);
                        }
                    }

                    filteredPersonas.setValue(finalList);
                }
            });
        });

        filteredPersonas.addSource(personasByName, asc -> {
            if(asc == null){
                asc = true;
            }

            List<MainListPersona> personas = getFilteredPersonas().getValue();
            if(personas != null){
                if(personas.size() == 1){
                    return;
                }

                if(asc){

                    Collections.sort(personas, sortByPersonaNameAsc);
                }
                else{
                    Collections.sort(personas, sortByPersonaNameDesc);
                }
            }

            filteredPersonas.setValue(personas);
        });

        filteredPersonas.addSource(personasByLevel, asc -> {
            if(asc == null){
                asc = true;
            }

            final List<MainListPersona> personas = getFilteredPersonas().getValue();

            if(personas != null){
                if(personas.size() == 1){
                    return;
                }

                if(asc){
                    Collections.sort(personas, sortByPersonaLevelAsc);
                }
                else{
                    Collections.sort(personas, sortByPersonaLevelDesc);
                }
            }

            filteredPersonas.setValue(personas);
        });

        filteredPersonas.addSource(personasByArcana, asc -> {
            if(asc == null){
                asc = true;
            }

            List<MainListPersona> personas = getFilteredPersonas().getValue();

            if(personas != null){
                if(personas.size() == 1){
                    return;
                }

                if(asc){
                    Collections.sort(personas, sortByPersonaArcanaAsc);
                }
                else{
                    Collections.sort(personas, sortByPersonaArcanaDesc);
                }
            }

            filteredPersonas.setValue(personas);
        });

        filteredPersonas.addSource(personaFilterArgs, (final PersonaFilterArgs inputFilterArgs) -> {

            allPersonas.observeForever(new Observer<List<MainListPersona>>() {
                @Override
                public void onChanged(@Nullable List<MainListPersona> personasToFilter) {

                    PersonaFilterArgs filterArgs = inputFilterArgs;

                    if(filterArgs == null){
                        filterArgs = new PersonaFilterArgs();
                    }

                    List<MainListPersona> finalValue = new ArrayList<>();

                    if(personasToFilter != null) {
                        for (MainListPersona persona : personasToFilter) {

                            if (persona.rare && !filterArgs.rarePersona) {
                                continue;
                            }

                            if (persona.dlc && !filterArgs.dlcPersona) {
                                continue;
                            }

                            if (filterArgs.arcana != Enumerations.Arcana.ANY && persona.arcana != filterArgs.arcana) {
                                continue;
                            }

                            if (persona.level < filterArgs.minLevel || persona.level > filterArgs.maxLevel) {
                                continue;
                            }

                            finalValue.add(persona);
                        }
                    }

                    filteredPersonas.setValue(finalValue);
                }
            });
        });

        this.repository = repository;

        repository.getAllPersonasForMainList().observeForever(personas -> {
            updatePersonas(personas);
        });
    }

    public void updatePersonas(List<MainListPersona> newPersonas){
        allPersonas.setValue(new ArrayList<>(newPersonas));
        filteredPersonas.setValue(new ArrayList<>(newPersonas));
    }

    public void filterPersonas(final String personaNameQuery){

        this.personaSearchName.postValue(personaNameQuery);
    }

    public LiveData<List<MainListPersona>> getFilteredPersonas() {
        return filteredPersonas;
    }

    public void sortPersonasByName(final boolean asc){
        personasByName.setValue(asc);
    }

    public void sortPersonasByLevel(final boolean asc){
        personasByLevel.setValue(asc);
    }

    public void sortPersonasByArcana(boolean asc) {
        personasByArcana.setValue(asc);
    }

    public void filterPersonas(final PersonaFilterArgs filterArgs) {
        personaFilterArgs.setValue(filterArgs);
    }
}
