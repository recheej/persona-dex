package com.persona5dex.viewmodels;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.PersonaFilters;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.GameType;
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
    private LiveData<List<MainListPersona>> allPersonas;

    private LiveData<List<MainListPersona>> filteredPersonas;

    public PersonaMainListViewModel(final MainPersonaRepository repository, ArcanaNameProvider arcanaNameProvider){
        this.repository = repository;
        this.arcanaNameProvider = arcanaNameProvider;

        allPersonas = repository.getAllPersonasForMainList();
        allPersonas.observeForever(mainListPersonas -> {

        });

        filteredPersonas = Transformations.switchMap(allPersonas, personas -> {
            MediatorLiveData<List<MainListPersona>> filteredLiveData = new MediatorLiveData<>();

            filteredLiveData.addSource(personaSearchName, searchValue -> {
                if(searchValue == null || searchValue.isEmpty()){
                    filteredLiveData.setValue(personas);
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

                    filteredLiveData.setValue(finalList);
                }
            });

            filteredLiveData.addSource(personasByName, asc -> {
                if(asc == null){
                    asc = true;
                }

                List<MainListPersona> newPersonas = filteredPersonas.getValue();
                if(newPersonas != null){
                    if(newPersonas.size() == 1){
                        return;
                    }

                    if(asc){
                        Collections.sort(newPersonas, sortByPersonaNameAsc);
                    }
                    else{
                        Collections.sort(newPersonas, sortByPersonaNameDesc);
                    }
                }

                filteredLiveData.setValue(newPersonas);
            });

            filteredLiveData.addSource(personasByLevel, asc -> {
                if(asc == null){
                    asc = true;
                }

                final List<MainListPersona> newPersonas = filteredPersonas.getValue();

                if(newPersonas != null){
                    if(newPersonas.size() == 1){
                        return;
                    }

                    if(asc){
                        Collections.sort(newPersonas, sortByPersonaLevelAsc);
                    }
                    else{
                        Collections.sort(newPersonas, sortByPersonaLevelDesc);
                    }
                }

                filteredLiveData.setValue(newPersonas);
            });

            filteredLiveData.addSource(personasByArcana, asc -> {
                if(asc == null){
                    asc = true;
                }

                List<MainListPersona> newPersonas = filteredLiveData.getValue();

                if(newPersonas != null){
                    if(newPersonas.size() == 1){
                        return;
                    }

                    if(asc){
                        Collections.sort(newPersonas, sortByPersonaArcanaAsc);
                    }
                    else{
                        Collections.sort(newPersonas, sortByPersonaArcanaDesc);
                    }
                }

                filteredLiveData.setValue(newPersonas);
            });

            filteredLiveData.addSource(personaFilterArgs, (final PersonaFilterArgs inputFilterArgs) -> {

                PersonaFilterArgs filterArgs = inputFilterArgs;

                if(filterArgs == null){
                    filterArgs = new PersonaFilterArgs();
                }

                List<MainListPersona> finalValue = new ArrayList<>();

                if(personas != null) {
                    final List<MainListPersona> newPersonas = PersonaFilters.filterGameType(personas, inputFilterArgs.gameType);

                    for (MainListPersona persona : newPersonas) {

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

                filteredLiveData.setValue(finalValue);
            });

            return filteredLiveData;
        });

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

    public void filterPersonas(@NonNull GameType gameType) {
        PersonaFilterArgs newFilterArgs  = personaFilterArgs.getValue();
        if(newFilterArgs == null){
            newFilterArgs = new PersonaFilterArgs();
        }
        newFilterArgs.gameType = gameType;
        personaFilterArgs.setValue(newFilterArgs);
    }
}
