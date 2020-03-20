package com.persona5dex;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.viewmodels.SettingsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Rechee on 8/13/2017.
 */

@RunWith(RobolectricTestRunner.class)
public class PersonaSettingsViewModelTest {

    private SettingsViewModel viewModel;
    private MutableLiveData<List<MainListPersona>> dlcPersonas;

    @Before
    public void init() {
        MainListPersona testPersona = new MainListPersona();
        testPersona.setName("testName");
        testPersona.id = 1;
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.dlc = true;
        testPersona.level = 1;

        MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.setName("testNameTwo");
        testPersonaTwo.id = 2;
        testPersonaTwo.arcana = Enumerations.Arcana.HANGED_MAN;
        testPersonaTwo.dlc = true;
        testPersonaTwo.level = 2;

        List<MainListPersona> personas = new ArrayList<>();
        personas.add(testPersona);
        personas.add(testPersonaTwo);

        dlcPersonas = new MutableLiveData<>();
        dlcPersonas.setValue(personas);

        MainPersonaRepository mainPersonaRepository = mock(MainPersonaRepository.class);
        when(mainPersonaRepository.getDLCPersonas()).thenReturn(dlcPersonas);

        this.viewModel = new SettingsViewModel(mainPersonaRepository);
    }

    @Test
    public void getDLCForSettings_NoError() throws Exception {
        viewModel.getDLCPersonaForSettings().observeForever(new Observer<String[][]>() {
            @Override
            public void onChanged(@Nullable String[][] output) {
                assertNotNull(output);
                assertTrue(output.length == 2);

                assertNotNull(output[0]);
                assertTrue(output[0].length != 0);
                assertNotNull(output[1]);
                assertTrue(output[1].length != 0);
            }
        });
    }

    @Test
    public void getDLCForSettings_IsSorted() throws Exception {

        MainListPersona testPersona = new MainListPersona();
        testPersona.setName("b");
        testPersona.id = 1;
        testPersona.arcana = Enumerations.Arcana.CHARIOT;
        testPersona.dlc = true;
        testPersona.level = 1;

        MainListPersona testPersonaTwo = new MainListPersona();
        testPersonaTwo.setName("a");
        testPersonaTwo.id = 2;
        testPersonaTwo.arcana = Enumerations.Arcana.HANGED_MAN;
        testPersonaTwo.dlc = true;
        testPersonaTwo.level = 2;

        List<MainListPersona> personas = new ArrayList<>();
        personas.add(testPersona);
        personas.add(testPersonaTwo);

        dlcPersonas.setValue(personas);

        viewModel.getDLCPersonaForSettings().observeForever(new Observer<String[][]>() {
            @Override
            public void onChanged(@Nullable String[][] output) {
                assertTrue(output[0][0].compareTo(output[0][1]) < 0);
            }
        });
    }

    //
    @Test
    public void getDLCForSettings_LengthsEqual() throws Exception {

        viewModel.getDLCPersonaForSettings().observeForever(new Observer<String[][]>() {
            @Override
            public void onChanged(@Nullable String[][] output) {
                assertNotNull(output);
                assertTrue(output.length == 2);

                assertEquals(output[0].length, output[1].length);
            }
        });
    }

    //
    @Test
    public void getDLCForSettings_HandlesNoDLC() throws Exception {

        dlcPersonas.setValue(new ArrayList<MainListPersona>(1));
        viewModel.getDLCPersonaForSettings().observeForever(new Observer<String[][]>() {
            @Override
            public void onChanged(@Nullable String[][] output) {
                assertNotNull(output);
                assertTrue(output.length == 2);

                assertNotNull(output[0]);
                assertTrue(output[0].length == 0);
                assertNotNull(output[1]);
                assertTrue(output[1].length == 0);
            }
        });
    }

    @Test
    public void getDLCForSettings_HandlesNullDLC() throws Exception {

        dlcPersonas.setValue(null);
        viewModel.getDLCPersonaForSettings().observeForever(new Observer<String[][]>() {
            @Override
            public void onChanged(@Nullable String[][] output) {
                assertNotNull(output);
                assertTrue(output.length == 2);

                assertNotNull(output[0]);
                assertTrue(output[0].length == 0);
                assertNotNull(output[1]);
                assertTrue(output[1].length == 0);
            }
        });
    }
}
