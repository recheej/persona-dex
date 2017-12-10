package com.persona5dex.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaFusionViewModel extends ViewModel {

    @Inject
    PersonaDisplayEdgesRepository repository;

    private LiveData<List<PersonaEdgeDisplay>> personaEdges;
    private int personaID;

    public PersonaFusionViewModel(PersonaDisplayEdgesRepository repository){
        this.repository = repository;
    }

    public PersonaFusionViewModel() {}

    public void init(Persona5ApplicationComponent component, int personaID, boolean isToList) {
        component
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);

        this.personaID = personaID;

        if(personaEdges == null){
            if(isToList){
                personaEdges = repository.getEdgesToPersona(personaID);
            }
            else{
                personaEdges = repository.getEdgesFromPersona(personaID);
            }
        }
    }

    public LiveData<List<PersonaEdgeDisplay>> getEdges(boolean toEdges){
        if(toEdges){
            return repository.
        }
    }
}
