package com.persona5dex;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[]{
                testPersonaTwo, testPersona
        });

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToSort);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);

        Observer observer = mock(Observer.class);
        viewModel.getFilteredPersonas().observeForever(observer);
        viewModel.sortPersonasByName(true);

        final List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();
        assertEquals(personasToSort.size(), output.size());
        assertEquals(testPersona, output.get(0));
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToSort);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));
        viewModel.sortPersonasByName(false);

        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();

        assertEquals(personasToSort.size(), output.size());
        assertEquals(testPersonaTwo.id, output.get(0).id);
        assertEquals(testPersonaTwo.arcana, output.get(0).arcana);
        assertEquals(testPersonaTwo.name, output.get(0).name);
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToSort);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));
        viewModel.sortPersonasByLevel(true);

        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();

        assertEquals(testPersonaTwo, output.get(0));
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToSort);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));
        viewModel.sortPersonasByLevel(false);


        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();

        assertNotNull(output);
        assertEquals(testPersonaTwo.id, output.get(0).id);
        assertEquals(testPersona.arcana, output.get(0).arcana);
        assertEquals(testPersona.name, output.get(0).name);
    }

    @Test
    public void filterPersonas_handlesEmptyList() throws Exception {

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(new ArrayList<>());

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
        viewModel.filterPersonas(filterArgs);

        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();

        assertNotNull(output);
        assertTrue(output.size() == 0);
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToFilter);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
        viewModel.filterPersonas(filterArgs);

        List<MainListPersona> filteredPersonas = viewModel.getFilteredPersonas().getValue();

        assertNotNull(filteredPersonas);
        assertTrue(filteredPersonas.size() == 1);
        assertTrue(filteredPersonas.get(0).arcana == Enumerations.Arcana.CHARIOT);
        assertTrue(filteredPersonas.get(0).name.equals(testPersonaOne.name));
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToFilter);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(testPersonaOne.level, testPersonaOne.level, Enumerations.Arcana.CHARIOT);
        viewModel.filterPersonas(filterArgs);

        List<MainListPersona> filteredPersonas = viewModel.getFilteredPersonas().getValue();

        assertNotNull(filteredPersonas);
        assertTrue(filteredPersonas.size() == 1);
        assertTrue(filteredPersonas.get(0).arcana == Enumerations.Arcana.CHARIOT);
        assertTrue(filteredPersonas.get(0).name.equals(testPersonaOne.name));
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToFilter);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
                99,
                Enumerations.Arcana.CHARIOT, true, false);

        viewModel.filterPersonas(filterArgs);

        List<MainListPersona> filteredPersonas = viewModel.getFilteredPersonas().getValue();

        assertNotNull(filteredPersonas);
        assertTrue(filteredPersonas.size() == 1);
        assertTrue(filteredPersonas.get(0).name.equals(testPersonaTwo.name));
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

        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(personasToFilter);

        MainPersonaRepository fakeMainPersonaRepository = mock(MainPersonaRepository.class);
        when(fakeMainPersonaRepository.getAllPersonasForMainList()).thenReturn(data);

        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(fakeMainPersonaRepository);
        viewModel.getFilteredPersonas().observeForever(mock(Observer.class));

        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
                99,
                Enumerations.Arcana.CHARIOT, false, true);
        viewModel.filterPersonas(filterArgs);

        List<MainListPersona> mainListPersonas = viewModel.getFilteredPersonas().getValue();

        assertNotNull(mainListPersonas);
        assertTrue(mainListPersonas.size() == 1);
        assertTrue(mainListPersonas.get(0).name.equals(testPersonaTwo.name));
    }
}
