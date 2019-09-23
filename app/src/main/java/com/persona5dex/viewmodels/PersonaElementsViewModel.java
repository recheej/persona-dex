package com.persona5dex.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.room.PersonaElement;
import com.persona5dex.repositories.PersonaElementsRepository;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaElementsViewModel extends ViewModel {

    @Inject
    PersonaElementsRepository repository;

    private LiveData<List<PersonaElement>> personaElements;
    private int personaID;

    public PersonaElementsViewModel(PersonaElementsRepository repository){
        this.repository = repository;
    }

    public PersonaElementsViewModel() {}

    public void init(Persona5ApplicationComponent component, int personaID) {
        component
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .inject(this);

        this.personaID = personaID;

        if(personaElements == null){
            personaElements = repository.getElementsForPersona(personaID);
        }
    }

    public LiveData<HashMap<Enumerations.Element, Enumerations.ElementEffect>> getElementsForPersona(int personaID){
        if(personaElements == null){
            personaElements = repository.getElementsForPersona(personaID);
        }

        return Transformations.map(personaElements, new Function<List<PersonaElement>, HashMap<Enumerations.Element, Enumerations.ElementEffect>>() {
            @Override
            public HashMap<Enumerations.Element, Enumerations.ElementEffect> apply(List<PersonaElement> input) {
                HashMap<Enumerations.Element, Enumerations.ElementEffect> elementsMap = new HashMap<>(20);
                for (PersonaElement personaElement : input) {
                    if(!elementsMap.containsKey(personaElement.element)){
                        elementsMap.put(personaElement.element, personaElement.effect);
                    }
                }

                return elementsMap;
            }
        });
    }
}
