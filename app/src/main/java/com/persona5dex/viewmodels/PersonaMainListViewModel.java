package com.persona5dex.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaMainListViewModel extends ViewModel{
    private List<MainListPersona> allPersonas;
    private MutableLiveData<List<MainListPersona>> filteredPersonas;
    private LiveData<List<MainListPersona>> mainListPersonaLiveData;

    @Inject
    MainPersonaRepository repository;

    private Comparator<MainListPersona> sortByPersonaNameDesc;
    private Comparator<MainListPersona> sortByPersonaNameAsc;
    private Comparator<MainListPersona> sortByPersonaLevelAsc;
    private Comparator<MainListPersona> sortByPersonaLevelDesc;
    private Comparator<MainListPersona> sortByPersonaArcanaAsc;
    private Comparator<MainListPersona> sortByPersonaArcanaDesc;

    public PersonaMainListViewModel(MainPersonaRepository repository){
        this.repository = repository;
        this.init();
    }

    public PersonaMainListViewModel() {}

    public void init() {
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

        mainListPersonaLiveData = repository.getPersonasForMainList();
        filteredPersonas = new MutableLiveData<>();

        mainListPersonaLiveData.observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> personas) {
                allPersonas = personas;
                filteredPersonas.setValue(personas);
            }
        });
    }

    public void inject(Persona5ApplicationComponent component){
        component
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);
        this.init();
    }

    public void filterPersonas(final String personaNameQuery){

        List<MainListPersona> finalValue = new ArrayList<>();
        List<MainListPersona> input = allPersonas;
        if(input != null ){

            if(personaNameQuery == null || personaNameQuery.isEmpty()){
                //if the search is blank, return all personas
                finalValue.addAll(input);
            }
            else{
                for (MainListPersona mainListPersona : input) {
                    if(mainListPersona.name.toLowerCase().contains(personaNameQuery.toLowerCase())){
                        finalValue.add(mainListPersona);
                    }
                }
            }
        }

        filteredPersonas.setValue(finalValue);
    }

    public LiveData<List<MainListPersona>> getFilteredPersonas() {
        if(filteredPersonas == null){
            filteredPersonas = new MutableLiveData<>();
        }

        return filteredPersonas;
    }

    public void sortPersonasByName(final boolean asc){

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
    }

    public void sortPersonasByLevel(final boolean asc){
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
    }

    public void filterPersonas(final PersonaFilterArgs filterArgs) {

        List<MainListPersona> personasToFilter = allPersonas;

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

    public void sortPersonasByArcana(boolean asc) {

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
    }
}
