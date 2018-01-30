package com.persona5dex;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Rechee on 12/30/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersonaUtilitiesTest {

//    /**
//     * Tests that if you switch locales, arcana names come back correctly
//     */
//    @Test
//    public void arcanaNamesWithDifferentLocaleTest() {
//        ClassLoader classLoader = getClass().getClassLoader();
//
//        InputStream personaDataStream = classLoader.getResourceAsStream("person_data.json");
//
//        Locale turkishLocale = Locale.forLanguageTag("tr-TR");
//        Locale.setDefault(turkishLocale);
//
//        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(new Gson());
//
//        Persona[] allPersonas = personaFileUtilities.allPersonas(personaDataStream);
//
//        for (Persona persona : allPersonas) {
//            assertNotNull(persona.arcana);
//        }
//    }
}
