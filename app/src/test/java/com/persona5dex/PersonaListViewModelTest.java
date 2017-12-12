package com.persona5dex;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.FakeMainPersonaRepository;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Rechee on 8/13/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = Persona5Application.class)
public class PersonaListViewModelTest {

    @Test
    public void testSortByNameAsc() throws Exception {

        final MainListPersona testPersona = new MainListPersona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 2;

        MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "B";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[] {
                testPersonaTwo, testPersona
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToSort);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.sortPersonasByName(true);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> output) {
                assertEquals(personasToSort.size(), output.size());
                assertEquals(testPersona, output.get(0));
            }
        });
    }

    @Test
    public void testSortByNameDesc() throws Exception {

        MainListPersona testPersona = new MainListPersona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 2;

        final MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "B";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[] {
                testPersona, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToSort);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.sortPersonasByName(false);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> output) {
                assertEquals(personasToSort.size(), output.size());
                assertEquals(testPersonaTwo, output.get(0));
            }
        });
    }

    @Test
    public void testSortByLevelAsc() throws Exception {

        MainListPersona testPersona = new MainListPersona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 2;

        final MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "A";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 1;

        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[] {
                testPersona, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToSort);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.sortPersonasByLevel(true);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> personasToSort) {
                assertEquals(testPersonaTwo, personasToSort.get(0));
            }
        });
    }

    @Test
    public void testSortByLevelDesc() throws Exception {

        MainListPersona testPersona = new MainListPersona();
        testPersona.name = "A";
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.level = 1;

        final MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "A";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[] {
                testPersona, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToSort);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.sortPersonasByLevel(false);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> personasToSort) {
                assertEquals(testPersonaTwo, personasToSort.get(0));
            }
        });
    }

    @Test
    public void filterPersonas_handlesEmptyList() throws Exception {

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(new ArrayList<MainListPersona>());

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
        viewModel.filterPersonas(filterArgs);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> filteredPersonas) {

                assertNotNull(filteredPersonas);
                assertTrue(filteredPersonas.size() == 0);
            }
        });
    }

    @Test
    public void filterPersonas_filtersByArcana() throws Exception {

        final MainListPersona testPersonaOne = new MainListPersona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;

        MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.DEATH;
        testPersonaTwo.level = 2;

        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[] {
            testPersonaOne, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToFilter);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
        viewModel.filterPersonas(filterArgs);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> filteredPersonas) {
                assertNotNull(filteredPersonas);
                assertTrue(filteredPersonas.size() == 1);
                assertTrue(filteredPersonas.get(0).arcana == Enumerations.Arcana.CHARIOT);
                assertTrue(filteredPersonas.get(0).name.equals(testPersonaOne.name));
            }
        });
    }

    @Test
    public void filterPersonas_filtersByLevel() throws Exception {

        final MainListPersona testPersonaOne = new MainListPersona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;

        MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[] {
                testPersonaOne, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToFilter);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(testPersonaOne.level, testPersonaOne.level, Enumerations.Arcana.CHARIOT);
        viewModel.filterPersonas(filterArgs);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> filteredPersonas) {
                assertNotNull(filteredPersonas);
                assertTrue(filteredPersonas.size() == 1);
                assertTrue(filteredPersonas.get(0).level == testPersonaOne.level);
                assertTrue(filteredPersonas.get(0).name.equals(testPersonaOne.name));
            }
        });
    }

    @Test
    public void filterPersonas_filtersOutDLC() throws Exception {

        MainListPersona testPersonaOne = new MainListPersona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;
        testPersonaOne.dlc = true;

        final MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[] {
                testPersonaOne, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToFilter);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
                99,
                Enumerations.Arcana.CHARIOT, true, false);

        viewModel.filterPersonas(filterArgs);
        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> filteredPersonas) {
                assertNotNull(filteredPersonas);
                assertTrue(filteredPersonas.size() == 1);
                assertTrue(filteredPersonas.get(0).name.equals(testPersonaTwo.name));
            }
        });
    }

    @Test
    public void filterPersonas_filtersOutRare() throws Exception {

        final MainListPersona testPersonaOne = new MainListPersona();
        testPersonaOne.name = "testPersonaOne";
        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaOne.level = 1;
        testPersonaOne.rare = true;

        final MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.name = "testPersonaTwo";
        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
        testPersonaTwo.level = 2;

        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[] {
                testPersonaOne, testPersonaTwo
        });

        FakeMainPersonaRepository fakeMainPersonaRepository = new FakeMainPersonaRepository(personasToFilter);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
                99,
                Enumerations.Arcana.CHARIOT, false, true);
        viewModel.filterPersonas(filterArgs);

        viewModel.getFilteredPersonas().observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> mainListPersonas) {
                assertNotNull(mainListPersonas);
                assertTrue(mainListPersonas.size() == 1);
                assertTrue(mainListPersonas.get(0).name.equals(testPersonaTwo.name));
            }
        });
    }
}
