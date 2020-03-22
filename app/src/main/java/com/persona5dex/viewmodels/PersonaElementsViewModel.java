package com.persona5dex.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.dagger.application.AndroidViewModelRepositoryModule;
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

    PersonaElementsRepository repository;

    private LiveData<List<PersonaElement>> personaElements;

    public PersonaElementsViewModel(PersonaElementsRepository repository, int personaID) {
        this.repository = repository;
        personaElements = repository.getElementsForPersona(personaID);
    }

    public LiveData<HashMap<Enumerations.Element, Enumerations.ElementEffect>> getElementsForPersona() {
        return Transformations.map(personaElements, input -> {
            HashMap<Enumerations.Element, Enumerations.ElementEffect> elementsMap = new HashMap<>(20);
            for(PersonaElement personaElement : input) {
                if(!elementsMap.containsKey(personaElement.element)) {
                    elementsMap.put(personaElement.element, personaElement.effect);
                }
            }

            return elementsMap;
        });
    }
}
