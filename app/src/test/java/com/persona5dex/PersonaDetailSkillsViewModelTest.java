package com.persona5dex;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.Skill;
import com.persona5dex.repositories.PersonaSkillsRepository;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by reche on 1/5/2018.
 */
@RunWith(RobolectricTestRunner.class)
public class PersonaDetailSkillsViewModelTest {

    @Test
    public void getSkills_OrderedByLevel() {

        PersonaDetailSkill testSkillOne = new PersonaDetailSkill("test name one", 2,
                "effect", 1);

        PersonaDetailSkill testSkillTwo = new PersonaDetailSkill("test name two", 1,
                "effect", 2);

        List<PersonaDetailSkill> skillList = new ArrayList();
        skillList.add(testSkillOne);
        skillList.add(testSkillTwo);

        MutableLiveData<List<PersonaDetailSkill>> skillLiveData = new MutableLiveData<>();
        skillLiveData.setValue(skillList);

        PersonaSkillsRepository repository = mock(PersonaSkillsRepository.class);
        when(repository.getPersonaSkillsForDetail(anyInt())).thenReturn(skillLiveData);

        PersonaDetailSkillsViewModel viewModel = new PersonaDetailSkillsViewModel(repository);

        viewModel.getSkillsForPersona(1).observeForever(new Observer<List<PersonaDetailSkill>>() {
            @Override
            public void onChanged(@Nullable List<PersonaDetailSkill> personaDetailSkills) {
                assertTrue(personaDetailSkills.get(0).levelRequired < personaDetailSkills.get(1).levelRequired);
            }
        });
    }

    @Test
    public void getSkills_ReturnsExpectedData() {

        PersonaDetailSkill testSkillOne = new PersonaDetailSkill("test name one", 2,
                "effect", 1);

        final List<PersonaDetailSkill> skillList = new ArrayList();
        skillList.add(testSkillOne);

        MutableLiveData<List<PersonaDetailSkill>> skillLiveData = new MutableLiveData<>();
        skillLiveData.setValue(skillList);

        PersonaSkillsRepository repository = mock(PersonaSkillsRepository.class);
        when(repository.getPersonaSkillsForDetail(anyInt())).thenReturn(skillLiveData);

        PersonaDetailSkillsViewModel viewModel = new PersonaDetailSkillsViewModel(repository);

        viewModel.getSkillsForPersona(1).observeForever(new Observer<List<PersonaDetailSkill>>() {
            @Override
            public void onChanged(@Nullable List<PersonaDetailSkill> personaDetailSkills) {
                assertNotNull(personaDetailSkills);
                assertTrue(personaDetailSkills.size() > 0);
                assertEquals(skillList.size(), personaDetailSkills.size());
                assertEquals(skillList.get(0).skillID, personaDetailSkills.get(0).skillID);
            }
        });
    }

    @Test
    public void getSkill_ReturnsExpectedData() {

        final Skill testSkill = new Skill();
        testSkill.id = 1;
        testSkill.cost = 500;
        testSkill.element = "test element";
        testSkill.effect = "test effect";

        MutableLiveData<Skill> skillLiveData = new MutableLiveData<>();
        skillLiveData.setValue(testSkill);

        PersonaSkillsRepository repository = mock(PersonaSkillsRepository.class);
        when(repository.getSkill(testSkill.id)).thenReturn(skillLiveData);

        PersonaDetailSkillsViewModel viewModel = new PersonaDetailSkillsViewModel(repository);

        viewModel.getSkill(testSkill.id).observeForever(new Observer<Skill>() {
            @Override
            public void onChanged(@Nullable Skill skill) {
                assertNotNull(skill);
                assertEquals(testSkill.id, skill.id);
                assertEquals(testSkill.cost, skill.cost);
                assertEquals(testSkill.effect, skill.effect);
                assertEquals(testSkill.element, skill.element);
            }
        });
    }

    @Test
    public void getPersonasWithSkill_handlesEmpty() {

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(new ArrayList<>());
        PersonaSkillsRepository repository = mock(PersonaSkillsRepository.class);
        when(repository.getPersonasWithSkill(anyInt())).thenReturn(data);

        PersonaDetailSkillsViewModel viewModel = new PersonaDetailSkillsViewModel(repository);
        viewModel.getPersonasWithSkill(1).observeForever(personas -> {
            assertNotNull(personas);
            assertEquals(0, personas.size());
        });
    }
}
