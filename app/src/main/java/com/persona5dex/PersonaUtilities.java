package com.persona5dex;

import com.persona5dex.models.Enumerations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.persona5dex.models.Enumerations.*;

/**
 * Created by Rechee on 7/8/2017.
 */

public class PersonaUtilities {

    public static final String SHARED_PREF_FUSIONS = "personaFusions";
    public static final String SHARED_PREF_TRANSFER_CONTENT = "personaTransferContent";
    public static final String SHARED_PREF_DLC = "personaDLCContent";

    public static String normalizePersonaName(String personaName) {
        return personaName.replaceAll("[^a-zA-Z0-9]+", "").toLowerCase().trim();
    }
}
