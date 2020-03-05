package com.persona5dex.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.models.room.PersonaShadowName;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaMainListViewModel extends ViewModel{
    private final ArcanaNameProvider arcanaNameProvider;
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

    public PersonaMainListViewModel(final MainPersonaRepository repository, ArcanaNameProvider arcanaNameProvider){
        this.arcanaNameProvider = arcanaNameProvider;
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

        sortByPersonaNameAsc = (o1, o2) -> o1.name.compareTo(o2.name);

        sortByPersonaNameDesc = (o1, o2) -> o1.name.compareTo(o2.name) * -1;

        sortByPersonaLevelAsc = (o1, o2) -> Integer.compare(o1.level, o2.level);
        sortByPersonaLevelDesc = (o1, o2) -> Integer.compare(o1.level, o2.level) * -1;
        sortByPersonaArcanaAsc = (o1, o2) -> arcanaNameProvider.getArcanaNameForDisplay(o1.arcana).compareTo(arcanaNameProvider.getArcanaNameForDisplay(o2.arcana));
        sortByPersonaArcanaDesc = (o1, o2) -> arcanaNameProvider.getArcanaNameForDisplay(o1.arcana).compareTo(arcanaNameProvider.getArcanaNameForDisplay(o2.arcana)) * -1;

        filteredPersonas.addSource(personaSearchName, searchValue -> {
            allPersonas.observeForever(personas -> {

                if(personas == null){

                    personas = new ArrayList<>();
                }

                if(searchValue == null || searchValue.isEmpty()){
                    filteredPersonas.setValue(personas);
                }
                else{
                    List<MainListPersona> finalList = new ArrayList<>();

                    for (MainListPersona mainListPersona : personas) {
                        final String searchValueLower = searchValue.toLowerCase();

                        if(mainListPersona.name.toLowerCase().contains(searchValueLower)){
                            finalList.add(mainListPersona);
                        }
                        else{
                            for (PersonaShadowName personaShadowName : mainListPersona.personaShadowNames) {
                                if(personaShadowName.shadowName.toLowerCase().contains(searchValueLower)){
                                    finalList.add(mainListPersona);
                                    break;
                                }
                            }
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

            allPersonas.observeForever(personasToFilter -> {

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
            });
        });

        this.repository = repository;

        repository.getAllPersonasForMainList().observeForever(this::updatePersonas);
    }

    public void updatePersonas(List<MainListPersona> newPersonas){
        Collections.sort(newPersonas, (p1, p2) -> p1.name.compareTo(p2.name));
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
