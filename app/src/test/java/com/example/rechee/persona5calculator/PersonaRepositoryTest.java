package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PersonaRepositoryTest {

    @Test
    public void getAllPersonas_ReturnsEqualLength() throws Exception {
        Gson gson = new Gson();
        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(
                FakePersonaData.fakePersonaFileContents(), gson);

        Persona[] resultPersonas = personaRepositoryFile.allPersonas();
        assertNotNull(resultPersonas);
        assertTrue(resultPersonas.length > 0);
    }

    @Test
    public void getAllPersonas_HasData() throws Exception {
        Gson gson = new Gson();
        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(
                FakePersonaData.fakePersonaFileContents(), gson);

        Persona[] resultPersonas = personaRepositoryFile.allPersonas();
        for(Persona persona : resultPersonas){
            assertNotNull(persona.name);
            assertNotNull(persona.arcanaName);
            assertNotNull(persona.level);
            assertTrue(persona.level > -1);
        }
    }

    @Test
    public void getAllPersonas_IsSorted() throws Exception {
        Gson gson = new Gson();
        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(
                FakePersonaData.fakePersonaFileContents(), gson);

        Persona[] resultPersonas = personaRepositoryFile.allPersonas();
        assertTrue(resultPersonas[0].name.compareTo(resultPersonas[1].name) < 1);
    }

    @Test
    public void testPersonaFusions() throws Exception {
        Gson gson = new Gson();
        PersonaRepositoryFile personaRepositoryFile = new PersonaRepositoryFile(
                FakePersonaData.fakePersonaFileContents(), gson);

        Persona[] resultPersonas = personaRepositoryFile.allPersonas();
        assertTrue(resultPersonas[0].name.compareTo(resultPersonas[1].name) < 1);
    }
}