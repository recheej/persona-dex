package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rechee on 8/13/2017.
 */

public class PersonaListViewModelTest {
    @Test
    public void testSortByNameAsc() throws Exception {

        Persona testPersona = new Persona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 2;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "B";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToSort = new Persona[] {testPersonaTwo, testPersona};

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(personasToSort);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);
        viewModel.sortPersonasByName(personasToSort, true);

        assertEquals(testPersona, personasToSort[0]);
    }

    @Test
    public void testSortByNameDesc() throws Exception {

        Persona testPersona = new Persona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 2;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "B";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToSort = new Persona[] {testPersona, testPersonaTwo};

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(personasToSort);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);
        viewModel.sortPersonasByName(personasToSort, false);

        assertEquals(testPersonaTwo, personasToSort[0]);
    }

    @Test
    public void testSortByLevelAsc() throws Exception {

        Persona testPersona = new Persona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 2;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "A";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToSort = new Persona[] {testPersona, testPersonaTwo};

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(personasToSort);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);
        viewModel.sortPersonasByLevel(personasToSort, true);

        assertEquals(testPersonaTwo, personasToSort[0]);
    }

    @Test
    public void testSortByLevelDesc() throws Exception {

        Persona testPersona = new Persona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 1;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "A";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToSort = new Persona[] {testPersona, testPersonaTwo};
        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(personasToSort);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);
        viewModel.sortPersonasByLevel(personasToSort, false);

        assertEquals(testPersonaTwo, personasToSort[0]);
    }
}
