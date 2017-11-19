package com.persona5dex.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.persona5dex.Persona5Application;
import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaMainListViewModel extends AndroidViewModel {
    private MutableLiveData<List<MainListPersona>> mainListPersonas;
    private MutableLiveData<List<MainListPersona>> filterPersonas;

    @Inject
    MainPersonaRepository repository;

    private final Comparator<MainListPersona> sortByPersonaNameDesc;
    private final Comparator<MainListPersona> sortByPersonaNameAsc;
    private final Comparator<MainListPersona> sortByPersonaLevelAsc;
    private final Comparator<MainListPersona> sortByPersonaLevelDesc;
    private final Comparator<MainListPersona> sortByPersonaArcanaAsc;
    private final Comparator<MainListPersona> sortByPersonaArcanaDesc;

    public PersonaMainListViewModel(@NonNull Application application) {
        super(application);

        Persona5Application persona5Application = (Persona5Application) application;

        persona5Application.getComponent()
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);

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

        mainListPersonas = new MutableLiveData<>();
        filterPersonas = new MutableLiveData<>();
    }

    public LiveData<List<MainListPersona>> getMainListPersonas(){

        return Transformations.switchMap(repository.getPersonasForMainList(), new Function<List<MainListPersona>, LiveData<List<MainListPersona>>>() {
            @Override
            public LiveData<List<MainListPersona>> apply(List<MainListPersona> input) {

                if(input == null){
                    mainListPersonas.setValue(new ArrayList<MainListPersona>());
                }
                else{
                    mainListPersonas.setValue(input);
                }

                return Transformations.map(mainListPersonas, new Function<List<MainListPersona>, List<MainListPersona>>() {
                    @Override
                    public List<MainListPersona> apply(List<MainListPersona> input) {
                        Collections.sort(input, new Comparator<MainListPersona>() {
                            @Override
                            public int compare(MainListPersona p1, MainListPersona p2) {
                                return p1.name.compareTo(p2.name);
                            }
                        });

                        filterPersonas.setValue(input);
                        return input;
                    }
                });
            }
        });
    }

    public void filterPersonas(String personaNameQuery){

        List<MainListPersona> finalValue = new ArrayList<>();

        final List<MainListPersona> input = mainListPersonas.getValue();

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

        filterPersonas.setValue(finalValue);
    }

    public LiveData<List<MainListPersona>> getFilteredPersonas() {
        return this.filterPersonas;
    }

    public void sortPersonasByName(boolean asc){

        List<MainListPersona> personas = filterPersonas.getValue();

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

        filterPersonas.setValue(personas);
    }

    public void sortPersonasByLevel(boolean asc){

        List<MainListPersona> personas = filterPersonas.getValue();

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

        filterPersonas.setValue(personas);
    }

    public void filterPersonas(PersonaFilterArgs filterArgs) {
        List<MainListPersona> finalValue = new ArrayList<>();

        final List<MainListPersona> personasToFilter = mainListPersonas.getValue();

        if(personasToFilter != null){
            for (MainListPersona persona : personasToFilter) {

                if(persona.rare && !filterArgs.rarePersona){
                    continue;
                }

                if(persona.dlc && !filterArgs.dlcPersona){
                    continue;
                }

                if(filterArgs.arcana != Enumerations.Arcana.ANY && persona.arcana != filterArgs.arcana){
                    continue;
                }

                if(persona.level < filterArgs.minLevel || persona.level > filterArgs.maxLevel){
                    continue;
                }

                finalValue.add(persona);
            }
        }


        filterPersonas.setValue(finalValue);
    }

    public void sortPersonasByArcana(boolean asc) {

        List<MainListPersona> personas = filterPersonas.getValue();

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

        filterPersonas.setValue(personas);
    }
}
