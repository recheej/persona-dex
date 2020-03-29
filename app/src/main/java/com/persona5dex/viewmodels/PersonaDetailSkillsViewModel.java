package com.persona5dex.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.Skill;
import com.persona5dex.repositories.PersonaSkillsRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailSkillsViewModel extends ViewModel {

    public static final int MAX_CONFIDANT_SKILL_VALUE = -1;

    private PersonaSkillsRepository repository;

    public PersonaDetailSkillsViewModel(final PersonaSkillsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<PersonaDetailSkill>> getSkillsForPersona(int personaID) {
        return Transformations.map(repository.getPersonaSkillsForDetail(personaID), new Function<List<PersonaDetailSkill>, List<PersonaDetailSkill>>() {
            @Override
            public List<PersonaDetailSkill> apply(List<PersonaDetailSkill> input) {

                Collections.sort(input, new Comparator<PersonaDetailSkill>() {
                    @Override
                    public int compare(PersonaDetailSkill o1, PersonaDetailSkill o2) {
                        if(o1.levelRequired == MAX_CONFIDANT_SKILL_VALUE || o2.levelRequired == MAX_CONFIDANT_SKILL_VALUE) {
                            if(o1.levelRequired == MAX_CONFIDANT_SKILL_VALUE && o2.levelRequired == MAX_CONFIDANT_SKILL_VALUE) {
                                return 0;
                            } else if(o1.levelRequired == MAX_CONFIDANT_SKILL_VALUE) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }

                        if(o1.levelRequired < o2.levelRequired) {
                            return -1;
                        }

                        if(o1.levelRequired > o2.levelRequired) {
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

    public LiveData<Skill> getSkill(int skillID) {
        return repository.getSkill(skillID);
    }

    public LiveData<List<MainListPersona>> getPersonasWithSkill(int skillID) {
        return repository.getPersonasWithSkillLiveData(skillID);
    }
}
