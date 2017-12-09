package com.persona5dex.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.PersonaElement;
import com.persona5dex.repositories.PersonaElementsRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailSkillsViewModel extends ViewModel {

    @Inject
    PersonaSkillsRepository repository;

    private LiveData<List<PersonaDetailSkill>> personaElements;
    private int personaID;

    public PersonaDetailSkillsViewModel(PersonaSkillsRepository repository){
        this.repository = repository;
    }

    public PersonaDetailSkillsViewModel() {}

    public void init(Persona5ApplicationComponent component, int personaID) {
        component
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);

        this.personaID = personaID;

        if(personaElements == null){
            personaElements = repository.getPersonaSkillsForDetail(personaID);
        }
    }

    public LiveData<List<PersonaDetailSkill>> getElementsForPersona(int personaID){
        if(personaElements == null){
            personaElements = repository.getPersonaSkillsForDetail(personaID);
        }

        return Transformations.map(personaElements, new Function<List<PersonaDetailSkill>, List<PersonaDetailSkill>>() {
            @Override
            public List<PersonaDetailSkill> apply(List<PersonaDetailSkill> input) {

                Collections.sort(input, new Comparator<PersonaDetailSkill>() {
                    @Override
                    public int compare(PersonaDetailSkill o1, PersonaDetailSkill o2) {
                        if(o1.levelRequired < o2.levelRequired){
                            return -1;
                        }

                        if(o1.levelRequired > o2.levelRequired){
                            return 1;
                        }

                        //if the levels are equal, compare by name;
                        return o1.name.compareTo(o2.name);
                    }
                });

                return input;
            }
        });
    }
}
