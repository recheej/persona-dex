package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.FakePersonaTranserRepository;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaFilterArgs;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;
import com.example.rechee.persona5calculator.viewmodels.SettingsViewModel;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Rechee on 8/13/2017.
 */

public class PersonaSettingsViewModelTest {
    @Test
    public void getDLCForSettings_NoError() throws Exception {

        PersonaTransferRepository transferRepository = new FakePersonaTranserRepository();

        SettingsViewModel viewModel = new SettingsViewModel(transferRepository);

        String[][] output = viewModel.getDLCPersonaForSettings();
        assertNotNull(output);
        assertTrue(output.length == 2);

        assertNotNull(output[0]);
        assertTrue(output[0].length != 0);
        assertNotNull(output[1]);
        assertTrue(output[1].length != 0);
    }

    @Test
    public void getDLCForSettings_IsSorted() throws Exception {

        PersonaTransferRepository transferRepository = new FakePersonaTranserRepository();

        SettingsViewModel viewModel = new SettingsViewModel(transferRepository);

        String[][] output = viewModel.getDLCPersonaForSettings();
        assertTrue(output[0][0].compareTo(output[0][1]) < 0);
    }

    @Test
    public void getDLCForSettings_LengthsEqual() throws Exception {

        PersonaTransferRepository transferRepository = new FakePersonaTranserRepository();

        SettingsViewModel viewModel = new SettingsViewModel(transferRepository);

        String[][] output = viewModel.getDLCPersonaForSettings();
        assertNotNull(output);
        assertTrue(output.length == 2);

        assertEquals(output[0].length, output[1].length);
    }

    @Test
    public void getDLCForSettings_HandlesNoDLC() throws Exception {

        //fake repository with 0 dlc
        PersonaTransferRepository transferRepository = new PersonaTransferRepository() {
            @Override
            public void storePersonaForDetail(Persona persona) {

            }

            @Override
            public Persona getDetailPersona() {
                return null;
            }

            @Override
            public void storePersonaForFusion(Persona personaForFusion) {

            }

            @Override
            public int getPersonaForFusion() {
                return 0;
            }

            @Override
            public String getPersonaName(int personaID) {
                return null;
            }

            @Override
            public void commit() {

            }

            @Override
            public void setPersonaIDs(Persona[] personas) {

            }

            @Override
            public Map<String, Integer> getDLCPersonaForSettings() {
                return new HashMap<>();
            }

            @Override
            public Set<String> getOwnedDlCPersonaIDs() {
                return null;
            }

            @Override
            public boolean rarePersonaAllowedInFusions() {
                return false;
            }
        };

        SettingsViewModel viewModel = new SettingsViewModel(transferRepository);

        String[][] output = viewModel.getDLCPersonaForSettings();
        assertNotNull(output);
        assertTrue(output.length == 2);

        assertNotNull(output[0]);
        assertTrue(output[0].length == 0);
        assertNotNull(output[1]);
        assertTrue(output[1].length == 0);
    }

    @Test
    public void getDLCForSettings_HandlesNullDLC() throws Exception {

        //fake repository that returns a null map for dlc
        PersonaTransferRepository transferRepository = new PersonaTransferRepository() {
            @Override
            public void storePersonaForDetail(Persona persona) {

            }

            @Override
            public Persona getDetailPersona() {
                return null;
            }

            @Override
            public void storePersonaForFusion(Persona personaForFusion) {

            }

            @Override
            public int getPersonaForFusion() {
                return 0;
            }

            @Override
            public String getPersonaName(int personaID) {
                return null;
            }

            @Override
            public void commit() {

            }

            @Override
            public void setPersonaIDs(Persona[] personas) {

            }

            @Override
            public Map<String, Integer> getDLCPersonaForSettings() {
                return null;
            }

            @Override
            public Set<String> getOwnedDlCPersonaIDs() {
                return null;
            }

            @Override
            public boolean rarePersonaAllowedInFusions() {
                return false;
            }
        };

        SettingsViewModel viewModel = new SettingsViewModel(transferRepository);

        String[][] output = viewModel.getDLCPersonaForSettings();
        assertNotNull(output);
        assertTrue(output.length == 2);

        assertNotNull(output[0]);
        assertTrue(output[0].length == 0);
        assertNotNull(output[1]);
        assertTrue(output[1].length == 0);
    }
}
