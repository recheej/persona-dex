package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;

/**
 * Created by Rechee on 7/8/2017.
 */

public interface PersonaSuggestionRepository {
    PersonaSearchSuggestion[] allSuggestions();
}
