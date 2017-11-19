package com.persona5dex;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Persona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.PersonaRepositoryFile;
import com.persona5dex.viewmodels.PersonaListViewModel;
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
        //viewModel.sortPersonasByName(personasToSort, true);

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
        //viewModel.sortPersonasByName(personasToSort, false);

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
        testPersonaTwo.level = 1;

        Persona[] personasToSort = new Persona[] {testPersona, testPersonaTwo};

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(personasToSort);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);
        //viewModel.sortPersonasByLevel(personasToSort, true);

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
        //viewModel.sortPersonasByLevel(personasToSort, false);

        assertEquals(testPersonaTwo, personasToSort[0]);
    }

    @Test
    public void filterPersonas_handlesEmptyList() throws Exception {

        Persona[] personasToFilter = new Persona[0];
        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(personasToFilter);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
        //Persona[] filteredPersonas =  viewModel.filterPersonas(filterArgs, personasToFilter);

        //assertNotNull(filteredPersonas);
        //assertTrue(filteredPersonas.length == 0);
    }

    @Test
    public void filterPersonas_filtersByArcana() throws Exception {

        Persona testPersonaOne = new Persona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.DEATH;
        testPersonaTwo.level = 2;

        Persona[] personasToFilter = new Persona[] {
            testPersonaOne, testPersonaTwo
        };

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(new Persona[0]);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
        //Persona[] filteredPersonas =  viewModel.filterPersonas(filterArgs, personasToFilter);

        //assertNotNull(filteredPersonas);
        //assertTrue(filteredPersonas.length == 1);
        //assertTrue(filteredPersonas[0].getArcana() == Enumerations.Arcana.CHARIOT);
        //assertTrue(filteredPersonas[0].name.equals(testPersonaOne.name));
    }

    @Test
    public void filterPersonas_filtersByLevel() throws Exception {

        Persona testPersonaOne = new Persona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToFilter = new Persona[] {
                testPersonaOne, testPersonaTwo
        };

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(new Persona[0]);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(testPersonaOne.level, testPersonaOne.level, Enumerations.Arcana.CHARIOT);
        //Persona[] filteredPersonas =  viewModel.filterPersonas(filterArgs, personasToFilter);

        //assertNotNull(filteredPersonas);
        //assertTrue(filteredPersonas.length == 1);
        //assertTrue(filteredPersonas[0].level == testPersonaOne.level);
        //assertTrue(filteredPersonas[0].name.equals(testPersonaOne.name));
    }

    @Test
    public void filterPersonas_filtersOutDLC() throws Exception {

        Persona testPersonaOne = new Persona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;
        testPersonaOne.dlc = true;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToFilter = new Persona[] {
                testPersonaOne, testPersonaTwo
        };

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(new Persona[0]);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
                99,
                Enumerations.Arcana.CHARIOT, true, false);
        //Persona[] filteredPersonas =  viewModel.filterPersonas(filterArgs, personasToFilter);

        //assertNotNull(filteredPersonas);
        //assertTrue(filteredPersonas.length == 1);
        //assertTrue(filteredPersonas[0].name.equals(testPersonaTwo.name));
    }

    @Test
    public void filterPersonas_filtersOutRare() throws Exception {

        Persona testPersonaOne = new Persona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;
        testPersonaOne.rare = true;

        Persona testPersonaTwo = new Persona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        Persona[] personasToFilter = new Persona[] {
                testPersonaOne, testPersonaTwo
        };

        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(new Persona[0]);

        PersonaListViewModel viewModel = new PersonaListViewModel(personaRepositoryFile, null);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
                99,
                Enumerations.Arcana.CHARIOT, false, true);
        //Persona[] filteredPersonas =  viewModel.filterPersonas(filterArgs, personasToFilter);

        //assertNotNull(filteredPersonas);
        //assertTrue(filteredPersonas.length == 1);
        //assertTrue(filteredPersonas[0].name.equals(testPersonaTwo.name));
    }
}
