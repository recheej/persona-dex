//package com.persona5dex;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//import androidx.lifecycle.Observer;
//import androidx.test.core.app.ApplicationProvider;
//
//import com.persona5dex.models.Enumerations;
//import com.persona5dex.models.GameType;
//import com.persona5dex.models.MainListPersona;
//import com.persona5dex.models.PersonaFilterArgs;
//import com.persona5dex.models.PersonaRepository;
//import com.persona5dex.viewmodels.PersonaMainListViewModel;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.robolectric.RobolectricTestRunner;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by Rechee on 8/13/2017.
// */
//
//@RunWith(RobolectricTestRunner.class)
//public class PersonaListViewModelTest {
//
//    private ArcanaNameProvider arcanaNameProvider;
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    @Before
//    public void setup() {
//        arcanaNameProvider = new ArcanaNameProvider(ApplicationProvider.getApplicationContext());
//    }
//
//    @Test
//    public void testSortByNameAsc() throws Exception {
//
//        final MainListPersona testPersona = getTestMainListPersona();
//        testPersona.setName("A");
//        testPersona.arcana = Enumerations.Arcana.CHARIOT;
//        testPersona.level = 2;
//
//        MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("B");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 2;
//
//        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[]{
//                testPersonaTwo, testPersona
//        });
//
//        PersonaRepository personaRepository = Mockito.mock(PersonaRepository.class);
//        Mockito.when(personaRepository.getPersonas())
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToSort);
//
//        Observer observer = Mockito.mock(Observer.class);
//        viewModel.getFilteredPersonas().observeForever(observer);
//        viewModel.sortPersonasByName(true);
//
//        final List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();
//        assertEquals(personasToSort.size(), output.size());
//        assertEquals(testPersona, output.get(0));
//    }
//
//    private MainListPersona getTestMainListPersona() {
//        MainListPersona mainListPersona = new MainListPersona();
//        mainListPersona.gameId = GameType.BASE;
//        return mainListPersona;
//    }
//
//    @Test
//    public void testSortByNameDesc() throws Exception {
//
//        MainListPersona testPersona = getTestMainListPersona();
//        testPersona.setName("A");
//        testPersona.arcana = Enumerations.Arcana.CHARIOT;
//        testPersona.level = 2;
//
//        final MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("B");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 2;
//
//        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[]{
//                testPersona, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToSort);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//        viewModel.sortPersonasByName(false);
//
//        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();
//
//        assertEquals(personasToSort.size(), output.size());
//        assertEquals(testPersonaTwo.id, output.get(0).id);
//        assertEquals(testPersonaTwo.arcana, output.get(0).arcana);
//        assertEquals(testPersonaTwo.getName(), output.get(0).getName());
//    }
//
//    @Test
//    public void testSortByLevelAsc() throws Exception {
//
//        MainListPersona testPersona = getTestMainListPersona();
//        testPersona.setName("A");
//        testPersona.arcana = Enumerations.Arcana.CHARIOT;
//        testPersona.level = 2;
//
//        final MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("A");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 1;
//
//        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[]{
//                testPersona, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToSort);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//        viewModel.sortPersonasByLevel(true);
//
//        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();
//
//        assertEquals(testPersonaTwo, output.get(0));
//    }
//
//    @Test
//    public void testSortByLevelDesc() throws Exception {
//
//        MainListPersona testPersona = getTestMainListPersona();
//        testPersona.setName("A");
//        testPersona.arcana = Enumerations.Arcana.CHARIOT;
//        testPersona.level = 1;
//
//        final MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("A");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 2;
//
//        final List<MainListPersona> personasToSort = Arrays.asList(new MainListPersona[]{
//                testPersona, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToSort);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//        viewModel.sortPersonasByLevel(false);
//
//        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();
//
//        assertNotNull(output);
//        assertEquals(testPersonaTwo.id, output.get(0).id);
//        assertEquals(testPersona.arcana, output.get(0).arcana);
//        assertEquals(testPersona.getName(), output.get(0).getName());
//    }
//
//    @Test
//    public void filterPersonas_handlesEmptyList() throws Exception {
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(new ArrayList<>());
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//
//        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
//        viewModel.filterPersonas(filterArgs);
//
//        List<MainListPersona> output = viewModel.getFilteredPersonas().getValue();
//
//        assertNotNull(output);
//        assertTrue(output.size() == 0);
//    }
//
//    @Test
//    public void filterPersonas_filtersByArcana() throws Exception {
//
//        final MainListPersona testPersonaOne = getTestMainListPersona();
//        testPersonaOne.setName("testPersonaOne");
//        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaOne.level = 1;
//
//        MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("testPersonaTwo");
//        testPersonaTwo.arcana = Enumerations.Arcana.DEATH;
//        testPersonaTwo.level = 2;
//
//        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[]{
//                testPersonaOne, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToFilter);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//
//        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1, 2, Enumerations.Arcana.CHARIOT);
//        viewModel.filterPersonas(filterArgs);
//
//        List<MainListPersona> filteredPersonas = viewModel.getFilteredPersonas().getValue();
//
//        assertNotNull(filteredPersonas);
//        assertTrue(filteredPersonas.size() == 1);
//        assertTrue(filteredPersonas.get(0).arcana == Enumerations.Arcana.CHARIOT);
//        assertTrue(filteredPersonas.get(0).getName().equals(testPersonaOne.getName()));
//    }
//
//    @Test
//    public void filterPersonas_filtersByLevel() throws Exception {
//
//        final MainListPersona testPersonaOne = getTestMainListPersona();
//        testPersonaOne.setName("testPersonaOne");
//        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaOne.level = 1;
//
//        MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("testPersonaTwo");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 2;
//
//        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[]{
//                testPersonaOne, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToFilter);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//
//        PersonaFilterArgs filterArgs = new PersonaFilterArgs(testPersonaOne.level, testPersonaOne.level, Enumerations.Arcana.CHARIOT);
//        viewModel.filterPersonas(filterArgs);
//
//        List<MainListPersona> filteredPersonas = viewModel.getFilteredPersonas().getValue();
//
//        assertNotNull(filteredPersonas);
//        assertTrue(filteredPersonas.size() == 1);
//        assertTrue(filteredPersonas.get(0).arcana == Enumerations.Arcana.CHARIOT);
//        assertTrue(filteredPersonas.get(0).getName().equals(testPersonaOne.getName()));
//    }
//
//    @Test
//    public void filterPersonas_filtersOutDLC() throws Exception {
//
//        MainListPersona testPersonaOne = getTestMainListPersona();
//        testPersonaOne.setName("testPersonaOne");
//        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaOne.level = 1;
//        testPersonaOne.dlc = true;
//
//        final MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("testPersonaTwo");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 2;
//
//        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[]{
//                testPersonaOne, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToFilter);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//
//        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
//                99,
//                Enumerations.Arcana.CHARIOT, false);
//
//        viewModel.filterPersonas(filterArgs);
//
//        List<MainListPersona> filteredPersonas = viewModel.getFilteredPersonas().getValue();
//
//        assertNotNull(filteredPersonas);
//        assertTrue(filteredPersonas.size() == 1);
//        assertTrue(filteredPersonas.get(0).getName().equals(testPersonaTwo.getName()));
//    }
//
//    @Test
//    public void filterPersonas_filtersOutRare() throws Exception {
//
//        final MainListPersona testPersonaOne = getTestMainListPersona();
//        testPersonaOne.setName("testPersonaOne");
//        testPersonaOne.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaOne.level = 1;
//        testPersonaOne.rare = true;
//
//        final MainListPersona testPersonaTwo = getTestMainListPersona();
//        testPersonaTwo.setName("testPersonaTwo");
//        testPersonaTwo.arcana = Enumerations.Arcana.CHARIOT;
//        testPersonaTwo.level = 2;
//
//        List<MainListPersona> personasToFilter = Arrays.asList(new MainListPersona[]{
//                testPersonaOne, testPersonaTwo
//        });
//
//        PersonaMainListViewModel viewModel = new PersonaMainListViewModel(arcanaNameProvider, GameType.BASE);
//        viewModel.initialize(personasToFilter);
//        viewModel.getFilteredPersonas().observeForever(Mockito.mock(Observer.class));
//
//        PersonaFilterArgs filterArgs = new PersonaFilterArgs(1,
//                99,
//                Enumerations.Arcana.CHARIOT, true);
//        viewModel.filterPersonas(filterArgs);
//
//        List<MainListPersona> mainListPersonas = viewModel.getFilteredPersonas().getValue();
//
//        assertNotNull(mainListPersonas);
//        assertTrue(mainListPersonas.size() == 1);
//        assertTrue(mainListPersonas.get(0).getName().equals(testPersonaTwo.getName()));
//    }
//}
