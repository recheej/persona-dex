package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepository;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepositoryFile;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PersonaSuggestionRepositoryTest {

    @Test
    public void getSuggestions_ReturnsData() throws Exception {
        Gson gson = new Gson();
        PersonaSuggestionRepository suggestionRepository = new PersonaSuggestionRepositoryFile(FakePersonaData.fakePersonaFileContents(),
                gson);

        PersonaSearchSuggestion[] suggestions = suggestionRepository.allSuggestions();
        assertNotNull(suggestions);
        assertTrue(suggestions.length > 0);
    }

    @Test
    public void getSuggestions_IsSorted() throws Exception {
        Gson gson = new Gson();
        PersonaSuggestionRepository suggestionRepository = new PersonaSuggestionRepositoryFile(FakePersonaData.fakePersonaFileContents(),
                gson);

        PersonaSearchSuggestion[] suggestions = suggestionRepository.allSuggestions();
        assertTrue(suggestions[0].name.compareTo(suggestions[1].name) < 1);
    }

    @Test
    public void getSuggestions_DataNotNull() throws Exception {
        Gson gson = new Gson();
        PersonaSuggestionRepository suggestionRepository = new PersonaSuggestionRepositoryFile(FakePersonaData.fakePersonaFileContents(),
                gson);

        PersonaSearchSuggestion[] suggestions = suggestionRepository.allSuggestions();
        for (PersonaSearchSuggestion suggestion : suggestions){
            assertNotNull(suggestion.arcana);
            assertNotNull(suggestion.name);
        }
    }
}